package com.semiashkin.komi.vyltrorjas.nerotolge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semiashkin.komi.vyltrorjas.AbstractIntegrationTest;
import com.semiashkin.komi.vyltrorjas.IntegrationTest;
import com.semiashkin.komi.vyltrorjas.dto.Language;
import com.semiashkin.komi.vyltrorjas.dto.NeurotolgeTranslateRequest;
import com.semiashkin.komi.vyltrorjas.dto.NeurotolgeTranslateResponse;
import com.semiashkin.komi.vyltrorjas.util.JsonHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

public class NeurotolgeIntegrationTest implements AbstractIntegrationTest {

    @Autowired
    private RestClient restClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.neurotolge.host}")
    private String neurotolgeTranslateUri;

    @Test
    public void testNeurotolgePublicApi() {
        NeurotolgeTranslateRequest request = NeurotolgeTranslateRequest.builder()
                .application("test")
                .domain("general")
                .src(Language.ENGLISH.getNeurotolgeCode())
                .tgt(Language.KOMI.getNeurotolgeCode())
                .text("Good!")
                .build();

        ResponseEntity<NeurotolgeTranslateResponse> responseEntity = restTemplate.postForEntity(
                neurotolgeTranslateUri + "/v2",
                new HttpEntity<>(request),
                NeurotolgeTranslateResponse.class
        );

        NeurotolgeTranslateResponse response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Бур!", response.getResult());
    }
}
