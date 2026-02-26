package com.semiashkin.komi.vyltrorjas.openai.client;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.errors.OpenAIException;
import com.openai.models.responses.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiNewsClient {

    @Value("${openai.api.key}")
    private String openAiKey;

    private OpenAIClient client;

    @PostConstruct
    public void init() {
        client = OpenAIOkHttpClient.builder()
            .apiKey(openAiKey)
            .build();
        log.info("OpenAI client initialized");
    }

    public String summarize(String topic) {
        String userInstruction = """
            Summarize today's news into a single JSON object.
            
            Date: %s (Europe/Tallinn)
            Optional topical focus (may be empty): %s
            
            Strict requirements:
            - Use JSON mode: return ONLY valid JSON (no markdown).
            - Schema:
              {
                "date": "YYYY-MM-DD",
                "source_count": number,
                "headline_count": number,
                "news": [ { "title": string, "text": string, "category": string, "sources": [string], "urls": [string] } ],
                "meta": { "note": string, "generated_by": "openai", "timezone": "Europe/Tallinn" }
              }
            - Deduplicate similar pieces of news across sources.
            - Prefer neutral wording and factual grouping.
            - Should be at least 8-10 news.
            - Text for each piece of the news must be at least 10 sentences.
            - If no news are available, return empty arrays but keep the schema.
            """.formatted(LocalDate.now(), topic == null ? "" : topic);

        // 3) Call OpenAI with JSON mode enabled
        try {
            ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-5-nano") // pick your preferred model
                .addTool(WebSearchTool.builder()
                    .type(WebSearchTool.Type.WEB_SEARCH_PREVIEW)
                    .build())
                .input(userInstruction)
//                .temperature(0.2)
//                .maxTokens(100000)
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


            // Pass through as application/json
            return json;

        } catch (OpenAIException ex) {
            throw new RuntimeException(ex);
//            return """
//            {
//              "date":"%s",
//              "source_count":0,
//              "headline_count":0,
//              "highlights":[],
//              "top_topics":[],
//              "meta":{"note":"OpenAI call failed: %s","generated_by":"openai","timezone":"Europe/Tallinn"}
//            }
//            """.formatted(isoDate, sanitize(ex.getMessage()));
        }
    }

    private static String sanitize(String in) {
        if (in == null) return "";
        return in.replace("\"", "'").replace("\n", " ").replace("\r", " ");
    }

    private record Headline(String title, String url, String source, Instant published) {
    }
}
