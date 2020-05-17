package matvey.realchess.telegram;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class UpdateTestData {

    public static User user(int id) {
        var user = mock(User.class);
        when(user.getId()).thenReturn(id);
        return user;
    }

    public static Update textUpdate(String text, int userId) {
        var update = mock(Update.class);
        var message = mock(Message.class);
        var user = user(userId);
        when(message.getFrom()).thenReturn(user);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(text);
        return update;
    }

    public static Update textUpdate(String text) {
        return textUpdate(text, ThreadLocalRandom.current().nextInt());
    }

    public static Update callbackQueryDataUpdate(String text, int userId) {
        var update = mock(Update.class);
        var callbackQuery = mock(CallbackQuery.class);
        var user = user(userId);
        when(callbackQuery.getFrom()).thenReturn(user);
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getData()).thenReturn(text);
        return update;
    }
}
