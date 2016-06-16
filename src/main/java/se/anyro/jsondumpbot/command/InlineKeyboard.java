package se.anyro.jsondumpbot.command;

import java.io.IOException;

import se.anyro.tgbotapi.TgBotApi;
import se.anyro.tgbotapi.types.Message;
import se.anyro.tgbotapi.types.inline.CallbackQuery;
import se.anyro.tgbotapi.types.reply_markup.InlineKeyboardButton;
import se.anyro.tgbotapi.types.reply_markup.InlineKeyboardMarkup;

/**
 * Inline keyboard for dumping callback data.
 */
public class InlineKeyboard extends Command {

    private static final String HIDE = "HIDE";

    private InlineKeyboardMarkup markup = InlineKeyboardMarkup.createVertical(
            InlineKeyboardButton.callbackData("Callback data", "Some data"),
            InlineKeyboardButton.callbackData("Hide keyboard", HIDE)
            );
    
    public InlineKeyboard(TgBotApi api) {
        super(api);
    }

    @Override
    public String getDescription() {
        return "Display an inline keyboard";
    }

    @Override
    public void run(Message message) throws IOException {
        api.sendMessage(message.chat.id, "Message with inline keyboard", null, false, 0, markup);
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        try {
            String data = callbackQuery.data;
            if (HIDE.equals(data)) {
                Message message = callbackQuery.message;
                api.editMessageText(message.chat.id, message.message_id, "Message without inline keyboard", null, true,
                        null);
            }
        } catch (IOException e) {
            if (api.isOwner(callbackQuery.from)) {
                api.debug(e);
            }
        }
    }
}