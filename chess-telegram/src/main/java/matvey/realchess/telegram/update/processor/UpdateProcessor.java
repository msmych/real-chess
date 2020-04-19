package matvey.realchess.telegram.update.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.Predicate;

public abstract class UpdateProcessor {

    protected static final Logger log = LoggerFactory.getLogger(UpdateProcessor.class);

    protected final TelegramLongPollingBot bot;

    public UpdateProcessor(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public final void process(Update update) {
        if (applies(update)) {
            doProcess(update);
        }
    }

    protected abstract boolean applies(Update update);

    protected abstract void doProcess(Update update);

    protected final boolean hasTextSuchThat(Update update, Predicate<String> predicate) {
        if (!update.hasMessage()) {
            return false;
        }
        var message = update.getMessage();
        if (!message.hasText()) {
            return false;
        }
        return predicate.test(message.getText());
    }

    protected final boolean isCommand(Update update, String command) {
        var commandText = command.startsWith("/") ? command.substring(1) : command;
        return hasTextSuchThat(update,
                text -> text.equals("/" + commandText) ||
                        text.equals("/" + commandText + "@" + bot.getBotUsername()));
    }

    protected final Message sendMessage(SendMessage sendMessage) {
        try {
            return bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Could not respond to message", e);
            throw new RuntimeException(e);
        }
    }

    protected final Message sendText(Message message, String text) {
        return sendMessage(new SendMessage(message.getChatId(), text));
    }
}
