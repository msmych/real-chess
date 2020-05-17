package matvey.realchess.telegram.update.processor;

import matvey.realchess.telegram.TelegramChessProps;
import matvey.realchess.telegram.datasource.ChessDataSource;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static matvey.realchess.telegram.UpdateTestData.textUpdate;
import static matvey.realchess.telegram.game.Game.startGame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameIdUpdateProcessorTest {

    private TelegramLongPollingBot bot = mock(TelegramLongPollingBot.class);
    private ChessDataSource dataSource = mock(ChessDataSource.class);
    private GameIdUpdateProcessor updateProcessor = new GameIdUpdateProcessor(bot, dataSource, new TelegramChessProps("test.chess.properties"));

    @Test
    void should_update_game_and_send_initial_boards() throws TelegramApiException {
        var gameWithPlayer1 = startGame().withPlayer1(456, 1111);
        when(dataSource.game(123)).thenReturn(Optional.of(gameWithPlayer1));
        when(dataSource.game(789)).thenReturn(Optional.of(gameWithPlayer1.withPlayer2(789, 2222)));
        var update = textUpdate("/123");
        updateProcessor.doProcess(update);
        assertThat(updateProcessor.appliesTo(update)).isTrue();
        updateProcessor.appliesTo(update);
        verify(bot).execute(any(SendMessage.class));
        verify(dataSource).update(anyInt(), any());
        verify(bot).execute(any(EditMessageText.class));
    }
}