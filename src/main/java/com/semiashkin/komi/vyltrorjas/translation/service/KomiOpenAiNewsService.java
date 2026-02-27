package com.semiashkin.komi.vyltrorjas.translation.service;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItemDto;
import com.semiashkin.komi.vyltrorjas.common.dto.Language;
import com.semiashkin.komi.vyltrorjas.common.service.FeedItemService;
import com.semiashkin.komi.vyltrorjas.openai.service.OpenAiNewsFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KomiOpenAiNewsService {

    private final OpenAiNewsFeedService openAiNewsFeedService;
    private final FeedTranslationService feedTranslationService;
    private final FeedItemService feedItemService;

    public List<FeedItemDto> getNews() {
        return getNews(openAiNewsFeedService.fetchFeed());
    }

    public List<FeedItemDto> getNews(Integer limit) {
        List<FeedItemDto> feed = openAiNewsFeedService.fetchFeed();

        if (limit < feed.size()) {
            feed = feed.subList(0, limit);
        }

        List<FeedItemDto> newsInKomi = getNews(feed);
        feedItemService.saveAll(newsInKomi);
        return newsInKomi;
    }

    private List<FeedItemDto> getNews(List<FeedItemDto> source) {
        return feedTranslationService.translateFeed(source, Language.ENGLISH, Language.KOMI);
    }
}
