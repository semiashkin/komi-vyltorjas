package com.semiashkin.komi.vyltrorjas.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedItem {

    private String title;
    private String pubDate;
    private String link;
    private List<String> links;
    private String text;
    private Language language;

}
