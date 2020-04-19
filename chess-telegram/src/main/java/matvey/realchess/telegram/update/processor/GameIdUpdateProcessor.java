package matvey.realchess.telegram.update.processor;

import matvey.realchess.telegram.datasource.ChessDataSource;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GameIdUpdateProcessor extends UpdateProcessor {

    private final ChessDataSource chessDataSource;

    public GameIdUpdateProcessor(TelegramLongPollingBot bot, ChessDataSource chessDataSource) {
        super(bot);
        this.chessDataSource = chessDataSource;
    }

    @Override
    protected boolean applies(Update update) {
        return hasTextSuchThat(update, this::isGameIdCommand);
    }

    @Override
    protected void doProcess(Update update) {

    }

    private boolean isGameIdCommand(String text) {
        if (!text.startsWith("/")) {
            return false;
        }
        int id;
        try {
            id = Integer.parseUnsignedInt(text.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }
        return chessDataSource.game(id).isPresent();
    }
}
