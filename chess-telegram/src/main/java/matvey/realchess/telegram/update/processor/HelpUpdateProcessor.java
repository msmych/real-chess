package matvey.realchess.telegram.update.processor;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpUpdateProcessor extends UpdateProcessor {

    public HelpUpdateProcessor(TelegramLongPollingBot bot) {
        super(bot);
    }

    @Override
    public void process(Update update) {
        if (isCommand(update, "help")) {
            respond(update.getMessage(), "Real chess help");
        }
    }
}
