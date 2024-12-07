package com.semiashkin.komi.vyltrorjas.euronews.client;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.zip.GZIPInputStream;

@Component
@RequiredArgsConstructor
public class EuronewsRssClient {

    @Value("${rss.euronews.uri}")
    private String euronewsRssUri;

    private final RestTemplate restTemplate;

    @SneakyThrows
    public SyndFeed getRssFeed() {
        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            HttpUriRequest request = new HttpGet(euronewsRssUri);
            try (CloseableHttpResponse response = client.execute(request);
                 InputStream stream = response.getEntity().getContent()) {
                SyndFeedInput input = new SyndFeedInput();

//                GZIPInputStream gzipInputStream = new GZIPInputStream(stream);
                XmlReader reader = new XmlReader(stream);
                return input.build(reader);
            }
        }
    }
}
