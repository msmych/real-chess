package matvey.realchess.telegram.update.processor;

import matvey.realchess.telegram.TelegramChessProps;
import matvey.realchess.telegram.datasource.ChessDataSource;
import matvey.realchess.telegram.game.Game;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static matvey.realchess.telegram.UpdateTestData.textUpdate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PlayUpdateProcessorTest {

    private TelegramLongPollingBot bot = mock(TelegramLongPollingBot.class);
    private ChessDataSource dataSource = mock(ChessDataSource.class);
    private PlayUpdateProcessor updateProcessor = new PlayUpdateProcessor(bot, dataSource, new TelegramChessProps("test.chess.properties"));

    @Test
    void should_create_game_and_send_id() throws TelegramApiException {
        var update = textUpdate("/play");
        assertThat(updateProcessor.appliesTo(update)).isTrue();
        updateProcessor.doProcess(update);
        verify(bot, times(3)).execute(any(SendMessage.class));
        verify(dataSource).save(anyInt(), any(Game.class));
        verify(dataSource).update(anyInt(), any());
    }
}