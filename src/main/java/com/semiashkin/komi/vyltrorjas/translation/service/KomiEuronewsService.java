package com.semiashkin.komi.vyltrorjas.translation.service;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItem;
import com.semiashkin.komi.vyltrorjas.common.dto.Language;
import com.semiashkin.komi.vyltrorjas.euronews.service.EuronewsRssFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KomiEuronewsService {

    private final EuronewsRssFeedService euronewsRssFeedService;
    private final FeedTranslationService feedTranslationService;

    public List<FeedItem> getNews() {
        return getNews(euronewsRssFeedService.fetchFeed());
    }

    public List<FeedItem> getNews(Integer limit) {
        List<FeedItem> feed = euronewsRssFeedService.fetchFeed();

        if (limit < feed.size()) {
            feed = feed.subList(0, limit);
        }
        return getNews(feed);
    }

    private List<FeedItem> getNews(List<FeedItem> source) {
        return feedTranslationService.translateFeed(source, Language.ENGLISH, Language.KOMI);
    }
}
