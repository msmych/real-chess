package matvey.realchess.telegram.update.processor;

import matvey.realchess.telegram.TelegramChessProps;
import matvey.realchess.telegram.datasource.ChessDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.telegram.UpdateTestData.callbackQueryDataUpdate;
import static matvey.realchess.telegram.game.Game.startGame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EndMoveUpdateProcessorTest {

    private static final int USER1_ID = 1111;
    private static final int USER2_ID = 2222;

    private TelegramLongPollingBot bot = mock(TelegramLongPollingBot.class);
    private ChessDataSource dataSource = mock(ChessDataSource.class);
    private TelegramChessProps props = new TelegramChessProps("test.chess.properties");

    private EndMoveUpdateProcessor updateProcessor = new EndMoveUpdateProcessor(bot, dataSource, props);

    @BeforeEach
    void setUp() {
        when(dataSource.game(1234)).thenReturn(Optional.of(startGame()
                .withPlayer1(USER1_ID, 1)
                .withPlayer2(USER2_ID, 2)));
    }

    @Test
    void shoud_move() {
        var update = callbackQueryDataUpdate("1234:c2c4", USER1_ID);
        assertThat(updateProcessor.appliesTo(update)).isTrue();
        updateProcessor.doProcess(update);
    }

    @Test
    void should_not_apply_to_wrong_format() {
        assertThat(updateProcessor.appliesTo(callbackQueryDataUpdate("1234:c2:c4", USER1_ID))).isFalse();
    }

    @Test
    void should_not_apply_to_absent_game() {
        when(dataSource.game(1234)).thenReturn(empty());
        assertThat(updateProcessor.appliesTo(callbackQueryDataUpdate("1234:c2c4", USER1_ID))).isFalse();
    }

    @Test
    void should_not_apply_to_another_user() {
        when(dataSource.game(1234)).thenReturn(Optional.of(startGame()
                .withPlayer1(USER1_ID, 1)
                .withPlayer2(USER1_ID, 2)));
        assertThat(updateProcessor.appliesTo(callbackQueryDataUpdate("1234:c2c4", USER2_ID))).isFalse();
    }
}