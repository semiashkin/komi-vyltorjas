package com.semiashkin.komi.vyltrorjas.telegram.bot;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItemDto;
import com.semiashkin.komi.vyltrorjas.translation.service.KomiOpenAiNewsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Objects;

@Component
public class KomiVyltorjasTelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final String botToken;
    private final TelegramClient telegramClient;
    private final KomiOpenAiNewsService komiOpenAiNewsService;

    public KomiVyltorjasTelegramBot(@Autowired KomiOpenAiNewsService komiOpenAiNewsService,
                                    @Value("${telegram.bot.token}") String botToken) {
        this.botToken = botToken;
        telegramClient = new OkHttpTelegramClient(getBotToken());
        this.komiOpenAiNewsService = komiOpenAiNewsService;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (!Objects.equals(messageText, "/news")) {
                sendMessage(chatId, "Command not recognized");
            } else {
                List<FeedItemDto> news = komiOpenAiNewsService.getNews(15);
                for (FeedItemDto feedItemDto : news) {


                    StringBuilder text = getStringBuilder(feedItemDto);

                    sendMessage(chatId, text.toString());
                }
            }
        }
    }

    @NotNull
    private static StringBuilder getStringBuilder(FeedItemDto feedItemDto) {
        StringBuilder text = new StringBuilder("*%s*\n\n%s\n".formatted(
            feedItemDto.getTitle(),
            feedItemDto.getText()
        ));

        if (feedItemDto.getLinks() != null && feedItemDto.getLinks().size() > 0) {
            text.append("\nӧшмӧсъяс:\n\n");

            StringBuilder links = new StringBuilder();

            feedItemDto.getLinks()
                .forEach(link ->
                    links.append("[%s](%s) ".formatted(link.getSource(), link.getUrl())));


            text.append(links);
        }
        return text;
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
            .chatId(chatId)
            .parseMode("Markdown")
            .text(text)
            .disableWebPagePreview(true)
            .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
    }
}
