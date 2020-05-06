package matvey.realchess.telegram.update.processor;

import matvey.realchess.telegram.Props;
import matvey.realchess.telegram.datasource.ChessDataSource;
import matvey.realchess.telegram.game.Game;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import static matvey.realchess.piece.Piece.Color.WHITE;
import static matvey.realchess.telegram.game.Game.startGame;

public class GameIdUpdateProcessor extends UpdateProcessor {

    private final ChessDataSource chessDataSource;
    private final Props props;

    public GameIdUpdateProcessor(TelegramLongPollingBot bot, ChessDataSource chessDataSource, Props props) {
        super(bot);
        this.chessDataSource = chessDataSource;
        this.props = props;
    }

    @Override
    protected boolean applies(Update update) {
        return hasTextSuchThat(update, this::isGameIdCommand);
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

    @Override
    protected void doProcess(Update update) {
        var message = update.getMessage();
        var gameId = Integer.parseUnsignedInt(message.getText().substring(1));
        var boardMessage = sendMessage(
            new SendMessage(message.getChatId(), props.color(WHITE) + "'s move")
                .setReplyMarkup(props.chessKeyboard(startGame().board())));
        chessDataSource.update(gameId, game -> game.withPlayer2(message.getFrom().getId(), boardMessage.getMessageId()));
        var opponentMessageId = chessDataSource.game(gameId)
            .flatMap(game -> game.whitePlayer().or(game::blackPlayer))
            .map(Game.Player::messageId)
            .orElseThrow();
        updateMessage(new EditMessageText()
            .setMessageId(opponentMessageId)
            .setText(props.color(WHITE) + "'s move")
            .setReplyMarkup(props.chessKeyboard(startGame().board())));
    }

}
