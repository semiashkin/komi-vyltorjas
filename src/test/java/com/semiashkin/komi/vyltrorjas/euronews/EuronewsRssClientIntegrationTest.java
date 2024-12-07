package com.semiashkin.komi.vyltrorjas.euronews;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.semiashkin.komi.vyltrorjas.AbstractIntegrationTest;
import com.semiashkin.komi.vyltrorjas.euronews.client.EuronewsRssClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EuronewsRssClientIntegrationTest implements AbstractIntegrationTest {

    @Autowired
    private EuronewsRssClient euronewsRssClient;

    @Test
    public void test() {
        SyndFeed rssFeed = euronewsRssClient.getRssFeed();

        assertNotNull(rssFeed);
        assertFalse(rssFeed.getEntries().isEmpty());

        rssFeed.getEntries()
            .stream()
            .map(SyndEntry::getTitle)
            .forEach(System.out::println);
    }
}
