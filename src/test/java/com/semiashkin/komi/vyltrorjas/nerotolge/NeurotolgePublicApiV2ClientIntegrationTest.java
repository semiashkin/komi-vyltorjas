package com.semiashkin.komi.vyltrorjas.nerotolge;

import com.semiashkin.komi.vyltrorjas.AbstractIntegrationTest;
import com.semiashkin.komi.vyltrorjas.client.NeurotolgePublicApiV2Client;
import com.semiashkin.komi.vyltrorjas.dto.Language;
import com.semiashkin.komi.vyltrorjas.dto.NeurotolgeTranslateRequest;
import com.semiashkin.komi.vyltrorjas.dto.NeurotolgeTranslateResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NeurotolgePublicApiV2ClientIntegrationTest implements AbstractIntegrationTest {

    @Autowired
    private NeurotolgePublicApiV2Client client;

    @Test
    public void testNeurotolgePublicApi() {
        NeurotolgeTranslateRequest request = NeurotolgeTranslateRequest.builder()
                .application("test")
                .domain("general")
                .src(Language.ENGLISH.getNeurotolgeCode())
                .tgt(Language.KOMI.getNeurotolgeCode())
                .text("Good!")
                .build();

        NeurotolgeTranslateResponse response = client.requestTranslate(request);

        Assertions.assertEquals("Бур!", response.getResult());
    }
}
