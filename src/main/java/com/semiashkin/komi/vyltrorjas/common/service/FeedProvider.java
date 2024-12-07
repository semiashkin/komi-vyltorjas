package com.semiashkin.komi.vyltrorjas.common.service;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItem;
import com.semiashkin.komi.vyltrorjas.common.dto.FeedProviderType;

import java.util.List;

public interface FeedProvider {

    List<FeedItem> fetchFeed();

    FeedProviderType getProviderType();

}
