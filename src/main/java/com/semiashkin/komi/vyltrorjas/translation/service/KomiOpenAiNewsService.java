package com.semiashkin.komi.vyltrorjas.translation.service;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItem;
import com.semiashkin.komi.vyltrorjas.common.dto.Language;
import com.semiashkin.komi.vyltrorjas.openai.service.OpenAiNewsFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KomiOpenAiNewsService {

    private final OpenAiNewsFeedService openAiNewsFeedService;
    private final FeedTranslationService feedTranslationService;

    public List<FeedItem> getNews() {
        return getNews(openAiNewsFeedService.fetchFeed());
    }

    public List<FeedItem> getNews(Integer limit) {
        List<FeedItem> feed = openAiNewsFeedService.fetchFeed();

        if (limit < feed.size()) {
            feed = feed.subList(0, limit);
        }
        return getNews(feed);
    }

    private List<FeedItem> getNews(List<FeedItem> source) {
        return feedTranslationService.translateFeed(source, Language.ENGLISH, Language.KOMI);
    }
}
