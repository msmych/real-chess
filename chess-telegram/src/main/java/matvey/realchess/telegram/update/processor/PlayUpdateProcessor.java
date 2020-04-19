package matvey.realchess.telegram.update.processor;

import matvey.realchess.telegram.Props;
import matvey.realchess.telegram.datasource.ChessDataSource;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;
import static matvey.realchess.telegram.game.Game.startGame;

public class PlayUpdateProcessor extends UpdateProcessor {

    private final ChessDataSource chessDataSource;
    private final Props props;

    public PlayUpdateProcessor(TelegramLongPollingBot bot, ChessDataSource chessDataSource, Props props) {
        super(bot);
        this.chessDataSource = chessDataSource;
        this.props = props;
    }

    @Override
    protected boolean applies(Update update) {
        return isCommand(update, "play");
    }

    @Override
    protected void doProcess(Update update) {
        var message = update.getMessage();
        sendText(message, "Send the following message to a player you want to play with:");
        var id = saveGame();
        sendText(message, "/" + id);
        var boardMessage = sendMessage(
                new SendMessage(message.getChatId(), "Waiting for another player:")
                        .setReplyMarkup(props.chessKeyboard(startGame().board())));
        chessDataSource.update(id,
                game -> game.withPlayer1(message.getFrom().getId(), boardMessage.getMessageId()));
    }

    private int saveGame() {
        int id;
        do {
            id = abs(ThreadLocalRandom.current().nextInt());
        } while (chessDataSource.save(id, startGame()).isPresent());
        return id;
    }
}
