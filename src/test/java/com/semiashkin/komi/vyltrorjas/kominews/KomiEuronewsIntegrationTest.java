package com.semiashkin.komi.vyltrorjas.kominews;

import com.semiashkin.komi.vyltrorjas.AbstractIntegrationTest;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedItem;
import com.semiashkin.komi.vyltrorjas.translation.service.KomiEuronewsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class KomiEuronewsIntegrationTest implements AbstractIntegrationTest {

    @Autowired
    private KomiEuronewsService komiEuronewsService;

    @Test
    public void test() {
        List<FeedItem> news = komiEuronewsService.getNews(5);

        Assertions.assertNotNull(news);

        news.stream()
            .map(FeedItem::getTitle)
            .forEach(System.out::println);
    }
}
