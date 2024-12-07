package com.semiashkin.komi.vyltrorjas.telegram.bot;

import com.semiashkin.komi.vyltrorjas.common.dto.FeedItem;
import com.semiashkin.komi.vyltrorjas.translation.service.KomiEuronewsService;
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
    private final KomiEuronewsService komiEuronewsService;

    public KomiVyltorjasTelegramBot(@Autowired KomiEuronewsService komiEuronewsService,
                                    @Value("${telegram.bot.token}") String botToken) {
        this.botToken = botToken;
        telegramClient = new OkHttpTelegramClient(getBotToken());
        this.komiEuronewsService = komiEuronewsService;
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
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (!Objects.equals(messageText, "/euronews")) {
                messageText = "Command not recognized";
            } else {
                List<FeedItem> news = komiEuronewsService.getNews(5);

                StringBuilder stringBuilder = new StringBuilder();

                for (FeedItem feedItem : news) {
                    stringBuilder.append("**%s**".formatted(feedItem.getTitle())).append("\n")
                        .append("__%s__".formatted(feedItem.getDescription())).append("\n").append("\n");
                }

                messageText = stringBuilder.toString();
            }

            SendMessage message = SendMessage // Create a message object
                .builder()
                .chatId(chatId)
                .text(messageText)
                .build();
            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
    }
}
