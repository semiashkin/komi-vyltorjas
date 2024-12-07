package com.semiashkin.komi.vyltrorjas.neurotolge.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NeurotolgeTranslateRequest {

    private String text;
    private String src;
    private String tgt;
    private String domain;
    private String application;

}
