package com.semiashkin.komi.vyltrorjas.openai.service;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItemDto;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedProviderType;
import com.semiashkin.komi.vyltrorjas.common.service.FeedProvider;
import com.semiashkin.komi.vyltrorjas.openai.client.OpenAiNewsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiNewsFeedService implements FeedProvider {

    private final OpenAiNewsClient openAiNewsClient;

    @Override
    public List<FeedItemDto> fetchFeed() {
        List<FeedItemDto> items = openAiNewsClient.getTheNews(null);
        log.info("Fetched {} news items from OpenAI", items.size());
        return items;
    }

    @Override
    public FeedProviderType getProviderType() {
        return FeedProviderType.OPENAI;
    }
}
