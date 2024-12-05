package com.semiashkin.komi.vyltrorjas.dto;

import lombok.Getter;

@Getter
public enum Language {

    ENGLISH("eng"), KOMI("kpv");

    private final String neurotolgeCode;

    Language(String neurotolgeCode) {
        this.neurotolgeCode = neurotolgeCode;
    }
}
