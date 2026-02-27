package com.semiashkin.komi.vyltrorjas.common.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("feed_item_link")
public class FeedItemLink {

    @Id
    private Long id;
    private Long feedItemId;
    private String source;
    private String url;


}
