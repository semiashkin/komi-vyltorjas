package com.semiashkin.komi.vyltrorjas.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedItem {

    private String title;
    private String pubDate;
    private String link;
    private String description;
    private Language language;

}
