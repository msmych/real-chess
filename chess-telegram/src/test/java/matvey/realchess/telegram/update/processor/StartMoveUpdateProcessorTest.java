package matvey.realchess.telegram.update.processor;

import matvey.realchess.telegram.TelegramChessProps;
import matvey.realchess.telegram.datasource.ChessDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.telegram.UpdateTestData.callbackQueryDataUpdate;
import static matvey.realchess.telegram.game.Game.startGame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StartMoveUpdateProcessorTest {

    private  static final int USER1_ID = 1111;
    private static final int USER2_ID = 2222;

    private TelegramLongPollingBot bot = mock(TelegramLongPollingBot.class);
    private ChessDataSource dataSource = mock(ChessDataSource.class);
    private TelegramChessProps telegramChessProps = new TelegramChessProps("test.chess.properties");

    private StartMoveUpdateProcessor updateProcessor = new StartMoveUpdateProcessor(bot, dataSource, telegramChessProps);

    @BeforeEach
    void setUp() {
        when(dataSource.game(1234)).thenReturn(Optional.of(startGame()
                .withPlayer1(USER1_ID, 1)
                .withPlayer2(USER2_ID, 2)));
    }

    @Test
    void should_set_start_move() throws TelegramApiException {
        when(dataSource.game(1234)).thenReturn(Optional.of(startGame()
                .withPlayer1(USER1_ID, 1)
                .withPlayer2(USER1_ID, 2)));
        var update = callbackQueryDataUpdate("1234:c2", USER1_ID);
        assertThat(updateProcessor.appliesTo(update)).isTrue();
        updateProcessor.doProcess(update);
        verify(bot, times(2)).execute(any(EditMessageText.class));
    }

    @Test
    void should_not_apply_to_wrong_format() {
        assertThat(updateProcessor.appliesTo(callbackQueryDataUpdate("c2", USER1_ID))).isFalse();
    }

    @Test
    void should_not_apply_to_absent_game() {
        when(dataSource.game(1234)).thenReturn(empty());
        assertThat(updateProcessor.appliesTo(callbackQueryDataUpdate("1234:c2", USER1_ID))).isFalse();
    }

    @Test
    void should_not_apply_to_another_user() {
        when(dataSource.game(1234)).thenReturn(Optional.of(startGame()
                .withPlayer1(USER1_ID, 1)
                .withPlayer2(USER1_ID, 2)));
        assertThat(updateProcessor.appliesTo(callbackQueryDataUpdate("1234:c2", USER2_ID))).isFalse();
    }

    @Test
    void should_not_apply_to_invalid_start_moves() {
        assertThat(updateProcessor.appliesTo(callbackQueryDataUpdate("1234:c4", USER1_ID))).isFalse();
        assertThat(updateProcessor.appliesTo(callbackQueryDataUpdate("1234:d7", USER1_ID))).isFalse();
        assertThat(updateProcessor.appliesTo(callbackQueryDataUpdate("1234:c2c4", USER1_ID))).isFalse();
    }
}