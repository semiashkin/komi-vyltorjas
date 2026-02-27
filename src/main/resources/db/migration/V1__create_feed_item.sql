create table if not exists feed_item (
    id bigserial primary key,
    title text,
    original_title text,
    topic text,
    text text,
    language text,
    item_time timestamptz,
    created_at timestamptz not null default now()
);

create index if not exists idx_feed_item_created_at on feed_item (created_at);
create index if not exists idx_feed_item_item_time on feed_item (item_time);

create table if not exists feed_item_link (
    id bigserial primary key,
    feed_item_id bigint not null,
    source text,
    url text,
    constraint fk_feed_item_link_feed_item
        foreign key (feed_item_id)
            references feed_item (id)
            on delete cascade
);

create index if not exists idx_feed_item_link_feed_item_id on feed_item_link (feed_item_id);
