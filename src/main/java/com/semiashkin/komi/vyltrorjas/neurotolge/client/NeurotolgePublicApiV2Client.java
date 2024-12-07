package com.semiashkin.komi.vyltrorjas.neurotolge.client;

import com.semiashkin.komi.vyltrorjas.neurotolge.dto.NeurotolgeTranslateRequest;
import com.semiashkin.komi.vyltrorjas.neurotolge.dto.NeurotolgeTranslateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NeurotolgePublicApiV2Client {

    @Value("${api.neurotolge.host}")
    private String neurotolgeTranslateUri;

    private final RestTemplate restTemplate;

    public NeurotolgeTranslateResponse requestTranslate(NeurotolgeTranslateRequest request) {
        ResponseEntity<NeurotolgeTranslateResponse> responseEntity = restTemplate.postForEntity(
                neurotolgeTranslateUri + "/v2",
                new HttpEntity<>(request),
                NeurotolgeTranslateResponse.class
        );

        return Optional.ofNullable(responseEntity.getBody())
                .orElseThrow(() -> new RuntimeException("Neurotõlge responded with empty body!"));
    }

}
