package se.anyro.jsondumpbot.command;

import java.io.IOException;

import se.anyro.tgbotapi.TgBotApi;
import se.anyro.tgbotapi.types.Message;

/**
 * Base class for bot commands such as /help and /about. By default the name of the command will be the name of the
 * class in lower case, but you can override getName() if you want another one.
 */
public abstract class Command {

    private final String NAME = getClass().getSimpleName().toLowerCase();

    protected TgBotApi api;

    public Command(TgBotApi api) {
        this.api = api;
    }

    public String getName() {
        return NAME;
    }

    public abstract String getDescription();

    public abstract void run(Message message) throws IOException;
}