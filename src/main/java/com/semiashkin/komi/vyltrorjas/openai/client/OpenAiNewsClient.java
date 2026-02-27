package com.semiashkin.komi.vyltrorjas.openai.client;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.errors.OpenAIException;
import com.openai.models.responses.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedItemDto;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedItemLinkDto;
import com.semiashkin.komi.vyltrorjas.common.dto.Language;
import com.semiashkin.komi.vyltrorjas.common.enums.NewsTopic;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiNewsClient {

    @Value("${openai.api.key}")
    private String openAiKey;

    private OpenAIClient client;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        client = OpenAIOkHttpClient.builder()
            .apiKey(openAiKey)
            .build();
        log.info("OpenAI client initialized");
    }

    public List<FeedItemDto> getTheNews(NewsTopic topic) {
        String userInstruction = """
            Summarize today's news into a single JSON object.
            
            Date: %s (Europe/Syktyvkar)
            Optional topical focus (may be empty): %s
            
            Strict requirements:
            - Use JSON mode: return ONLY valid JSON (no markdown).
            - Schema:
              {
                "date": "YYYY-MM-DD",
                "source_count": number,
                "headline_count": number,
                "news": [ { "title": string, "datetime": datetime,  "text": string, "topic": string, "links": [ { "source": string, "url": string } ] } ],
              }
            - Deduplicate similar pieces of news across sources.
            - Prefer neutral wording and factual grouping.
            - Should be at least 8-10 news.
            - Text for each piece of the news must be at least 10 sentences.
            - If no news are available, return empty arrays but keep the schema.
            - Datetime must have a format of like '2007-12-03T10:15:30'. No timezone, only what I provided.
            - The 'topic' value MUST BE one of these - %s
            """.formatted(LocalDate.now(), topic == null ? "" : topic, List.of(NewsTopic.values()));

        // 3) Call OpenAI with JSON mode enabled
        try {
            ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-5-nano") // pick your preferred model
                .addTool(WebSearchTool.builder()
                    .type(WebSearchTool.Type.WEB_SEARCH)
                    .build())
                .input(userInstruction)
                .build();

            Response response = client.responses()
                .create(params);

            String json = response.output()
                .stream()
                .map(ResponseOutputItem::message)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ResponseOutputMessage::content)
                .filter(list -> !list.isEmpty())
                .map(List::getFirst)
                .map(ResponseOutputMessage.Content::outputText)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .get()
                .text();


            return parseNews(json);

        } catch (OpenAIException ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<FeedItemDto> parseNews(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            String date = root.path("date").asText();

            List<FeedItemDto> items = new ArrayList<>();
            for (JsonNode newsNode : root.path("news")) {
                JsonNode linksNode = newsNode.path("links");

                List<FeedItemLinkDto> links = new ArrayList<>();
                if (linksNode.isArray() && !linksNode.isEmpty()) {
                    linksNode.forEach(urlv -> {
                        links.add(FeedItemLinkDto.builder()
                            .source(urlv.path("source").asText())
                            .url(urlv.path("url").asText())
                            .build());
                    });
                }

                items.add(FeedItemDto.builder()
                    .title(newsNode.path("title").asText())
                    .originalTitle(newsNode.path("title").asText())
                    .text(newsNode.path("text").asText())
                    .topic(NewsTopic.valueOf(newsNode.path("topic").asText()))
                    .itemTime(LocalDateTime.parse(newsNode.path("datetime").asText()))
                    .links(links)
                    .language(Language.ENGLISH)
                    .build());
            }

            return items;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse OpenAI news response", ex);
        }
    }
}
