package com.semiashkin.komi.vyltrorjas.common.dto;

import lombok.Getter;

@Getter
public enum Language {

    ENGLISH("eng"), KOMI("kpv");

    private final String neurotolgeCode;

    Language(String neurotolgeCode) {
        this.neurotolgeCode = neurotolgeCode;
    }
}
