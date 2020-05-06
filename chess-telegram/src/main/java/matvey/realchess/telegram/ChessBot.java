package matvey.realchess.telegram;

import matvey.realchess.telegram.datasource.ChessDataSource;
import matvey.realchess.telegram.update.processor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public class ChessBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(ChessBot.class);

    private final String token;
    private final Set<UpdateProcessor> updateProcessors;

    public ChessBot(String token, ChessDataSource chessDataSource, Props props) {
        this.token = token;
        this.updateProcessors = Set.of(
                new StartUpdateProcessor(this),
                new HelpUpdateProcessor(this),
                new PlayUpdateProcessor(this, chessDataSource, props),
                new GameIdUpdateProcessor(this, chessDataSource, props)
        );
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
