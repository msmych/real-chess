package matvey.realchess.telegram.update.processor;

import matvey.realchess.piece.Piece;
import matvey.realchess.telegram.TelegramChessProps;
import matvey.realchess.telegram.datasource.ChessDataSource;
import matvey.realchess.telegram.game.Game;
import matvey.realchess.telegram.game.Game.Player;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.lang.Integer.parseUnsignedInt;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWNV2;

public class StartMoveUpdateProcessor extends UpdateProcessor {

    private final ChessDataSource dataSource;
    private final TelegramChessProps telegramChessProps;

    public StartMoveUpdateProcessor(TelegramLongPollingBot bot, ChessDataSource dataSource, TelegramChessProps telegramChessProps) {
        super(bot);
        this.dataSource = dataSource;
        this.telegramChessProps = telegramChessProps;
    }

    @Override
    protected boolean appliesTo(Update update) {
        if (!hasCallbackQueryDataSuchThat(update, this::isValidStartMove)) {
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

    private boolean isValidStartMove(String data) {
        var parts = data.split(":");
        if (parts.length != 2) {
            return false;
        }
        if (parts[1].length() != 2) {
            return false;
        }
        if (parts[1].charAt(0) < 'a' || parts[1].charAt(1) > 'h') {
            return false;
        }
        if (parts[1].charAt(1) < '1' || parts[1].charAt(1) > '8') {
            return false;
        }
        return true;
    }

    @Override
    protected void doProcess(Update update) {
        var dataParts = update.getCallbackQuery().getData().split(":");
        var gameId = parseUnsignedInt(dataParts[0]);
        var game = dataSource.game(gameId).orElseThrow();
        updateMessage(new EditMessageText()
                .setMessageId(game.currentPlayer().map(Player::messageId).orElseThrow())
                .setText(telegramChessProps.color(game.board().currentMove()) + " `" + dataParts[1] + "`")
                .setParseMode(MARKDOWNV2)
                .setReplyMarkup(telegramChessProps.chess(game.board(), gameId, dataParts[1])));
        updateMessage(new EditMessageText()
                .setMessageId(game.waitingPlayer().map(Player::messageId).orElseThrow())
                .setText(telegramChessProps.color(game.board().waitingColor()))
                .setReplyMarkup(telegramChessProps.chessOpposite(game.board(), gameId, dataParts[1])));
    }
}
