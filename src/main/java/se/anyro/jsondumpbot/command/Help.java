package se.anyro.jsondumpbot.command;

import java.io.IOException;
import java.util.Collection;

import se.anyro.tgbotapi.TgBotApi;
import se.anyro.tgbotapi.types.Message;

/**
 * Listing all the available commands and their descriptions.
 */
public class Help extends Command {

    private String helpText;

    private Collection<Command> commands;

    public Help(TgBotApi api, Collection<Command> commands) {
        super(api);
        this.commands = commands;
    }

    @Override
    public String getDescription() {
        return "List all bot commands";
    }

    @Override
    public void run(Message message) throws IOException {
        if (helpText == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("This bot lets you see the JSON data received from your client.\n\n");
            builder.append("You can send any type of message you want or use one of the following commands:\n");
            for (Command command : commands) {
                // "/command_name - description"
                builder.append("/").append(command.getName()).append(" - ").append(command.getDescription())
                        .append('\n');
            }
            helpText = builder.toString();
        }
        api.sendMessage(message.chat.id, helpText);
    }
}
