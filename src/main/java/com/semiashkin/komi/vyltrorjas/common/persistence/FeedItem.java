package com.semiashkin.komi.vyltrorjas.common.persistence;

import com.semiashkin.komi.vyltrorjas.common.dto.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("feed_item")
public class FeedItem {

    @Id
    private Long id;
    private String title;
    private String originalTitle;
    private String topic;
    private LocalDateTime itemTime;
    private String text;
    private Language language;
    private Instant createdAt;

}
