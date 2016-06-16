package se.anyro.jsondumpbot.command;

import java.io.IOException;

import se.anyro.tgbotapi.TgBotApi;
import se.anyro.tgbotapi.types.Message;
import se.anyro.tgbotapi.types.reply_markup.KeyboardButton;
import se.anyro.tgbotapi.types.reply_markup.ReplyKeyboardMarkup;

/**
 * Keyboard for dumping contact and location.
 */
public class Keyboard extends Command {

    private static final ReplyKeyboardMarkup MARKUP = ReplyKeyboardMarkup.createVertical(
            KeyboardButton.textButton("Send text"),
            KeyboardButton.contactButton("Share contact"),
            KeyboardButton.locationButton("Share location"),
            KeyboardButton.textButton("/hidekeyboard")
            );

    public Keyboard(TgBotApi api) {
        super(api);
    }

    @Override
    public String getDescription() {
        return "Display a custom keyboard";
    }

    @Override
    public void run(Message message) throws IOException {
        api.sendMessage(message.chat.id, "Please select a command", null, false, 0, MARKUP);
    }
}