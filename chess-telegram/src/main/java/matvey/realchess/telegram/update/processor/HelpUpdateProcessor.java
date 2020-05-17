package matvey.realchess.telegram.update.processor;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpUpdateProcessor extends UpdateProcessor {

    public HelpUpdateProcessor(TelegramLongPollingBot bot) {
        super(bot);
    }

    @Override
    protected boolean appliesTo(Update update) {
        return isCommand(update, "help");
    }

    @Override
    protected void doProcess(Update update) {
        sendText(update.getMessage(), "Real chess help");
    }
}
