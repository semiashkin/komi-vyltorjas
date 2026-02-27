package com.semiashkin.komi.vyltrorjas.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedItemLinkDto {

    private String source;
    private String url;

}
