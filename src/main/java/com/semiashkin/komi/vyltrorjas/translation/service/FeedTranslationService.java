package com.semiashkin.komi.vyltrorjas.translation.service;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItemDto;
import com.semiashkin.komi.vyltrorjas.common.dto.Language;
import com.semiashkin.komi.vyltrorjas.majbyr.client.MajbyrPublicApiClient;
import com.semiashkin.komi.vyltrorjas.majbyr.dto.MajbyrTranslateRequest;
import com.semiashkin.komi.vyltrorjas.majbyr.dto.MajbyrTranslateResponse;
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

    private final MajbyrPublicApiClient majbyrClient;

    public List<FeedItemDto> translateFeed(List<FeedItemDto> feed, Language from, Language to) {
        log.info("Requested to translate %s news".formatted(feed.size()));
        AtomicInteger index = new AtomicInteger(0);
        List<FeedItemDto> list = feed.stream()
            .map(feedItem -> {
                String model = "smugri";

                MajbyrTranslateResponse titleResponse =
                    majbyrClient.requestTranslate(MajbyrTranslateRequest.builder()
                        .src(from.getCode())
                        .tgt(to.getCode())
                        .model(model)
                        .text(feedItem.getTitle())
                        .build());
                MajbyrTranslateResponse contentResponse =
                    majbyrClient.requestTranslate(MajbyrTranslateRequest.builder()
                        .src(from.getCode())
                        .tgt(to.getCode())
                        .model(model)
                        .text(feedItem.getText())
                        .build());

                log.info("Translated %s news".formatted(index.incrementAndGet()));

                return FeedItemDto.builder()
                    .language(to)
                    .itemTime(feedItem.getItemTime())
                    .title(titleResponse.getResult())
                    .topic(feedItem.getTopic())
                    .originalTitle(feedItem.getOriginalTitle())
                    .text(contentResponse.getResult())
                    .links(feedItem.getLinks())
                    .build();
            })
            .toList();

        log.info("Translation done");
        return list;
    }

}
