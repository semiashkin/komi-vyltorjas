package com.semiashkin.komi.vyltrorjas.majbyr.client;

import com.semiashkin.komi.vyltrorjas.majbyr.dto.MajbyrTranslateRequest;
import com.semiashkin.komi.vyltrorjas.majbyr.dto.MajbyrTranslateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MajbyrPublicApiClient {

    @Value("${api.majbyr.host}")
    private String majbyrTranslateUri;

    private final RestTemplate restTemplate;

    public MajbyrTranslateResponse requestTranslate(MajbyrTranslateRequest request) {
        ResponseEntity<MajbyrTranslateResponse> responseEntity = restTemplate.postForEntity(
                majbyrTranslateUri + "/translate/",
                new HttpEntity<>(request),
                MajbyrTranslateResponse.class
        );

        return Optional.ofNullable(responseEntity.getBody())
                .orElseThrow(() -> new RuntimeException("Majbyr responded with empty body!"));
    }

}
