package com.semiashkin.komi.vyltrorjas.common.service;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItemDto;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedItemLinkDto;
import com.semiashkin.komi.vyltrorjas.common.persistence.FeedItem;
import com.semiashkin.komi.vyltrorjas.common.persistence.FeedItemLink;
import com.semiashkin.komi.vyltrorjas.common.persistence.FeedItemLinkRepository;
import com.semiashkin.komi.vyltrorjas.common.persistence.FeedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedItemService {

    private final FeedItemRepository feedItemRepository;
    private final FeedItemLinkRepository feedItemLinkRepository;

    @Transactional
    public FeedItem save(FeedItemDto dto) {
        FeedItem item = new FeedItem();
        item.setTitle(dto.getTitle());
        item.setOriginalTitle(dto.getOriginalTitle());
        item.setTopic(dto.getTopic() == null ? null : dto.getTopic().name());
        item.setItemTime(dto.getItemTime());
        item.setText(dto.getText());
        item.setLanguage(dto.getLanguage());
        item.setCreatedAt(Instant.now());

        FeedItem saved = feedItemRepository.save(item);

        if (dto.getLinks() != null && !dto.getLinks().isEmpty()) {
            for (FeedItemLinkDto linkDto : dto.getLinks()) {
                FeedItemLink link = new FeedItemLink();
                link.setFeedItemId(saved.getId());
                link.setSource(linkDto.getSource());
                link.setUrl(linkDto.getUrl());
                feedItemLinkRepository.save(link);
            }
        }

        return saved;
    }

    @Transactional
    public void saveAll(List<FeedItemDto> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        for (FeedItemDto item : items) {
            save(item);
        }
    }

}
