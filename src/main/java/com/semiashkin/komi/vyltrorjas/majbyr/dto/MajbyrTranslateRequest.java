package com.semiashkin.komi.vyltrorjas.majbyr.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MajbyrTranslateRequest {

    private String text;
    private String src;
    private String tgt;
    private String model;

}
