package matvey.realchess.telegram.update.processor;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartUpdateProcessor extends UpdateProcessor {

    public StartUpdateProcessor(TelegramLongPollingBot bot) {
        super(bot);
    }

    @Override
    protected boolean applies(Update update) {
        return isCommand(update, "start");
    }

    @Override
    protected void doProcess(Update update) {
        sendText(update.getMessage(), """
                Hey
                I am real chess
                """);
    }
}
