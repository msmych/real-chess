package matvey.realchess.telegram;

import matvey.realchess.telegram.datasource.InMemoryChessDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class ChessBotApp {

    private static final Logger log = LoggerFactory.getLogger(ChessBotApp.class);

    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        new TelegramBotsApi().registerBot(
                new ChessBot(args[0], new InMemoryChessDataSource(), new Props("chess.properties")));
        log.info("Поехали");
    }
}
