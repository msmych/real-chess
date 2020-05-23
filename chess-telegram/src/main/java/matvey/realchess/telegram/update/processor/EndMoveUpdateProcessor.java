package matvey.realchess.telegram.update.processor;

import matvey.realchess.telegram.TelegramChessProps;
import matvey.realchess.telegram.datasource.ChessDataSource;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class EndMoveUpdateProcessor extends UpdateProcessor {

    private final ChessDataSource dataSource;
    private final TelegramChessProps props;

    public EndMoveUpdateProcessor(TelegramLongPollingBot bot, ChessDataSource dataSource, TelegramChessProps props) {
        super(bot);
        this.dataSource = dataSource;
        this.props = props;
    }

    @Override
    protected boolean appliesTo(Update update) {
        return false;
    }

    @Override
    protected void doProcess(Update update) {

    }
}
