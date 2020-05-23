package matvey.realchess.telegram.update.processor;

import matvey.realchess.piece.Piece;
import matvey.realchess.telegram.TelegramChessProps;
import matvey.realchess.telegram.datasource.ChessDataSource;
import matvey.realchess.telegram.game.Game;
import matvey.realchess.telegram.game.Game.Player;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.lang.Integer.parseUnsignedInt;

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
        if (!hasCallbackQueryDataSuchThat(update, this::isValidEndMove)) {
            return false;
        }
        var callbackQuery = update.getCallbackQuery();
        var dataParts = callbackQuery.getData().split(":");
        return dataSource.game(parseUnsignedInt(dataParts[0]))
                .filter(game -> game.currentPlayer()
                        .map(Player::userId)
                        .filter(userId -> userId.equals(callbackQuery.getFrom().getId()))
                        .isPresent())
                .map(Game::board)
                .filter(board -> board.squareAt(dataParts[1])
                        .piece()
                        .map(Piece::color)
                        .map(color -> color == board.currentMove())
                        .orElse(false))
                .isPresent();
    }

    private boolean isValidEndMove(String data) {
        var parts = data.split(":");
        if (parts.length != 2) {
            return false;
        }
        if (parts[1].length() != 4) {
            return false;
        }
        if (parts[1].charAt(0) < 'a' || parts[1].charAt(0) > 'h') {
            return false;
        }
        if (parts[1].charAt(1) < '1' || parts[1].charAt(1) > '8') {
            return false;
        }
        if (parts[1].charAt(2) < 'a' || parts[1].charAt(2) > 'h') {
            return false;
        }
        if (parts[1].charAt(3) < '1' || parts[1].charAt(3) > '8') {
            return false;
        }
        return true;
    }

    @Override
    protected void doProcess(Update update) {

    }
}
