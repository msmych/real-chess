package matvey.realchess.telegram;

import matvey.realchess.telegram.update.processor.HelpUpdateProcessor;
import matvey.realchess.telegram.update.processor.StartUpdateProcessor;
import matvey.realchess.telegram.update.processor.UpdateProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public class ChessBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(ChessBot.class);

    private final Set<UpdateProcessor> updateProcessors = Set.of(
            new StartUpdateProcessor(this),
            new HelpUpdateProcessor(this)
    );

    private final String token;

    public ChessBot(String token) {
        this.token = token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Received update {}", update);
        updateProcessors
                .forEach(updateProcessor -> updateProcessor.process(update));
    }

    @Override
    public String getBotUsername() {
        return "RealChessBot";
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
