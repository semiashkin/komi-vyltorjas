package com.semiashkin.komi.vyltrorjas.translation.service;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItem;
import com.semiashkin.komi.vyltrorjas.common.dto.Language;
import com.semiashkin.komi.vyltrorjas.neurotolge.client.NeurotolgePublicApiV2Client;
import com.semiashkin.komi.vyltrorjas.neurotolge.dto.NeurotolgeTranslateRequest;
import com.semiashkin.komi.vyltrorjas.neurotolge.dto.NeurotolgeTranslateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedTranslationService {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.domain}")
    private String appDomain;

    private final NeurotolgePublicApiV2Client neurotolgeClient;

    public List<FeedItem> translateFeed(List<FeedItem> feed, Language from, Language to) {
        log.info("Requested to translate %s news".formatted(feed.size()));
        AtomicInteger index = new AtomicInteger(0);
        List<FeedItem> list = feed.stream()
            .map(feedItem -> {
                NeurotolgeTranslateResponse titleResponse =
                    neurotolgeClient.requestTranslate(NeurotolgeTranslateRequest.builder()
                        .application(appName)
                        .domain(appDomain)
                        .src(from.getNeurotolgeCode())
                        .tgt(to.getNeurotolgeCode())
                        .text(feedItem.getTitle())
                        .build());
                NeurotolgeTranslateResponse contentResponse =
                    neurotolgeClient.requestTranslate(NeurotolgeTranslateRequest.builder()
                        .application(appName)
                        .domain(appDomain)
                        .src(from.getNeurotolgeCode())
                        .tgt(to.getNeurotolgeCode())
                        .text(feedItem.getDescription())
                        .build());

                log.info("Translated %s news".formatted(index.incrementAndGet()));

                return FeedItem.builder()
                    .language(to)
                    .pubDate(feedItem.getPubDate())
                    .title(titleResponse.getResult())
                    .description(contentResponse.getResult())
                    .build();
            })
            .toList();

        log.info("Translation done");
        return list;
    }

}
