package se.anyro.jsondumpbot.command;

import java.io.IOException;

import se.anyro.tgbotapi.TgBotApi;
import se.anyro.tgbotapi.types.Message;
import se.anyro.tgbotapi.types.reply_markup.ReplyKeyboardRemove;
import se.anyro.tgbotapi.types.reply_markup.ReplyMarkup;

public class HideKeyboard extends Command {

    private static final ReplyMarkup REPLY_KEYBOARD_HIDE = new ReplyKeyboardRemove();

    public HideKeyboard(TgBotApi api) {
        super(api);
    }

    @Override
    public String getDescription() {
        return "Hide the custom keyboard";
    }

    @Override
    public void run(Message message) throws IOException {
        api.sendMessage(message.chat.id, "Keyboard hidden", null, false, 0, REPLY_KEYBOARD_HIDE);
    }
}