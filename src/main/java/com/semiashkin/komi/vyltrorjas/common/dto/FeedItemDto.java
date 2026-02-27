package com.semiashkin.komi.vyltrorjas.common.dto;

import com.semiashkin.komi.vyltrorjas.common.enums.NewsTopic;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FeedItemDto {

    private String title;
    private String originalTitle;
    private NewsTopic topic;
    private List<FeedItemLinkDto> links;
    private LocalDateTime itemTime;
    private String text;
    private Language language;

}
