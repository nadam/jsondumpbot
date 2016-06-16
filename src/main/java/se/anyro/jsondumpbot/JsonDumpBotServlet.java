package se.anyro.jsondumpbot;

import static se.anyro.jsondumpbot.BuildVars.OWNER;
import static se.anyro.jsondumpbot.BuildVars.TOKEN;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.anyro.jsondumpbot.command.About;
import se.anyro.jsondumpbot.command.Command;
import se.anyro.jsondumpbot.command.Help;
import se.anyro.jsondumpbot.command.HideKeyboard;
import se.anyro.jsondumpbot.command.InlineKeyboard;
import se.anyro.jsondumpbot.command.Keyboard;
import se.anyro.tgbotapi.TgBotApi;
import se.anyro.tgbotapi.TgBotApi.ErrorListener;
import se.anyro.tgbotapi.types.Message;
import se.anyro.tgbotapi.types.ParseMode;
import se.anyro.tgbotapi.types.Update;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Main servlet class receiving messages and processing the commands.
 */
@SuppressWarnings("serial")
public class JsonDumpBotServlet extends HttpServlet implements ErrorListener {

    private TgBotApi api;
    private Map<String, Command> commands = new LinkedHashMap<>();
    private InlineKeyboard inlineKeyboard;

    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser JSON_PARSER = new JsonParser();

    public JsonDumpBotServlet() {
        super();
        api = new TgBotApi(TOKEN, OWNER, this);
        addCommand(new Help(api, commands.values()));
        addCommand(new Keyboard(api));
        inlineKeyboard = new InlineKeyboard(api);
        addCommand(inlineKeyboard);
        addCommand(new HideKeyboard(api));
        addCommand(new About(api));
        api.debug("Bot started");
    }

    private void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setStatus(200);

        try {
            String json = readFully(req.getReader());
            Update update = api.parseFromWebhook(json);

            api.sendMessage(update.fromUserId(), makePrettyJson(json), ParseMode.MARKDOWN, true, 0, null);

            if (update.isMessage()) {
                handleMessage(update.message);
            } else if (update.isCallbackQuery()) {
                inlineKeyboard.handleCallbackQuery(update.callback_query);
            }
        } catch (Exception e) {
            api.debug(e);
        }
    }

    private String readFully(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            builder.append(line).append('\n');
            line = reader.readLine();
        }
        return builder.toString();
    }

    public static String makePrettyJson(String updateJson) {
        JsonObject update = JSON_PARSER.parse(updateJson).getAsJsonObject();
        JsonObject message = (JsonObject) update.get("message");

        // Truncate text fields to avoid too long messages that can't be sent
        if (message != null) {
            truncateTextField(message);
            JsonObject replyToMessage = (JsonObject) message.get("reply_to_message");
            if (replyToMessage != null) {
                truncateTextField(replyToMessage);
            }
        }
        return "```\n" + PRETTY_GSON.toJson(update) + "```";
    }

    private static void truncateTextField(JsonObject message) {
        JsonPrimitive textField = (JsonPrimitive) message.getAsJsonObject().get("text");
        if (textField != null) {
            String text = textField.getAsString();
            if (text.length() > 35) {
                message.addProperty("text", text.substring(0, 32) + "...");
            }
        }
    }

    private void handleMessage(Message message) throws IOException {
        String text = message.text;
        if (text == null) {
            text = message.caption;
        }
        if (text != null && text.startsWith("/")) {
            String[] parts = text.substring(1).split(" ", 2);
            Command command = commands.get(parts[0]);
            if (command != null) {
                command.run(message);
                return;
            }
        }
    }

    @Override
    public void onError(int errorCode, String description) {
        api.debug(new Exception("ErrorCode " + errorCode + ", " + description));
    }
}