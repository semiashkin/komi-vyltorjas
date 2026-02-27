package com.semiashkin.komi.vyltrorjas.common.service;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItemDto;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedProviderType;

import java.util.List;

public interface FeedProvider {

    List<FeedItemDto> fetchFeed();

    FeedProviderType getProviderType();

}
