package se.anyro.jsondumpbot.command;

import java.io.IOException;

import se.anyro.tgbotapi.TgBotApi;
import se.anyro.tgbotapi.types.Message;
import se.anyro.tgbotapi.types.ParseMode;

public class About extends Command {

    private final String TEXT;

    public About(TgBotApi api) {
        super(api);
        TEXT = '*' + api.getBotName() + '*' + " shows the JSON data received from your client";
    }

    @Override
    public String getDescription() {
        return "Information about this bot";
    }

    @Override
    public void run(Message message) throws IOException {
        api.sendMessage(message.chat.id, TEXT, ParseMode.MARKDOWN, true, 0, null);
    }
}