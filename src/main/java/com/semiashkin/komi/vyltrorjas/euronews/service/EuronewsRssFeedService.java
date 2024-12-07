package com.semiashkin.komi.vyltrorjas.euronews.service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedItem;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedProviderType;
import com.semiashkin.komi.vyltrorjas.common.dto.Language;
import com.semiashkin.komi.vyltrorjas.common.service.FeedProvider;
import com.semiashkin.komi.vyltrorjas.euronews.client.EuronewsRssClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EuronewsRssFeedService implements FeedProvider {

    private final EuronewsRssClient euronewsRssClient;

    public List<FeedItem> fetchFeed() {
        SyndFeed rssFeed = euronewsRssClient.getRssFeed();

        List<FeedItem> feedItems = rssFeed.getEntries()
                .stream()
                .map(entry -> FeedItem.builder()
                        .title(entry.getTitle())
                        .link(entry.getLink())
                        .pubDate(entry.getPublishedDate().toString())
                        .description(entry.getDescription().getValue())
                        .language(Language.ENGLISH)
                        .build()
                )
                .toList();

        return feedItems;
    }

    @Override
    public FeedProviderType getProviderType() {
        return FeedProviderType.EURONEWS;
    }


}
