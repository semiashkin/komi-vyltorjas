package com.semiashkin.komi.vyltrorjas.common.config;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedProviderType;
import com.semiashkin.komi.vyltrorjas.common.service.FeedProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    @Bean
    public Map<FeedProviderType, FeedProvider> feedProvidersMap(@Autowired List<FeedProvider> providers) {
        return providers.stream()
                .collect(Collectors.toMap(FeedProvider::getProviderType, Function.identity()));
    }

}
