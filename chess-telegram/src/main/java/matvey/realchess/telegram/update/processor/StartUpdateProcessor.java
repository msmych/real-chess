package matvey.realchess.telegram.update.processor;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartUpdateProcessor extends UpdateProcessor {

    public StartUpdateProcessor(TelegramLongPollingBot bot) {
        super(bot);
    }

    @Override
    public void process(Update update) {
        if (isCommand(update, "start")) {
            respond(update.getMessage(), """
                    Hey
                    I am real chess
                    """);
        }
    }
}
