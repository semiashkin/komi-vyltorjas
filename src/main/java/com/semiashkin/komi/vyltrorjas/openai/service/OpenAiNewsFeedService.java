package com.semiashkin.komi.vyltrorjas.openai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedItem;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedProviderType;
import com.semiashkin.komi.vyltrorjas.common.dto.Language;
import com.semiashkin.komi.vyltrorjas.common.service.FeedProvider;
import com.semiashkin.komi.vyltrorjas.openai.client.OpenAiNewsClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiNewsFeedService implements FeedProvider {

    private final OpenAiNewsClient openAiNewsClient;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public List<FeedItem> fetchFeed() {
        String json = openAiNewsClient.summarize(null);
        JsonNode root = objectMapper.readTree(json);
        String date = root.path("date").asText();

        List<FeedItem> items = new ArrayList<>();
        for (JsonNode newsNode : root.path("news")) {
            String url = "";
            JsonNode urls = newsNode.path("urls");

            List<String> links = new ArrayList<>();
            if (urls.isArray() && !urls.isEmpty()) {
                url = urls.get(0).asText();
            }
            urls.forEach(urlv -> links.add(urlv.asText()));
            items.add(FeedItem.builder()
                    .title(newsNode.path("title").asText())
                    .text(newsNode.path("text").asText())
                    .link(url)
                    .links(links)
                    .pubDate(date)
                    .language(Language.ENGLISH)
                    .build());
        }

        log.info("Fetched {} news items from OpenAI", items.size());
        return items;
    }

    @Override
    public FeedProviderType getProviderType() {
        return FeedProviderType.OPENAI;
    }
}
