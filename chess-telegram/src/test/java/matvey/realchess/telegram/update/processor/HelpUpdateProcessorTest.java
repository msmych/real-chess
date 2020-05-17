package matvey.realchess.telegram.update.processor;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static matvey.realchess.telegram.UpdateTestData.textUpdate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class HelpUpdateProcessorTest {

    private TelegramLongPollingBot bot = mock(TelegramLongPollingBot.class);
    private HelpUpdateProcessor updateProcessor = new HelpUpdateProcessor(bot);

    @Test
    void should_send_greeting_on_start_command() throws TelegramApiException {
        var update = textUpdate("/help");
        assertThat(updateProcessor.appliesTo(update)).isTrue();
        updateProcessor.doProcess(update);
        verify(bot).execute(any(SendMessage.class));
    }

}