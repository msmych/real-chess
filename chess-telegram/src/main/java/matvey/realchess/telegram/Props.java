package matvey.realchess.telegram;

import matvey.realchess.Board;
import matvey.realchess.piece.Piece;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.Properties;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static matvey.realchess.Square.Color.LIGHT;

public final class Props {

    private final Properties properties = new Properties();

    public Props(String file) {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InlineKeyboardMarkup chessKeyboard(Board board) {
        return new InlineKeyboardMarkup(
                rangeClosed(1, 8)
                        .map(i -> 8 - i + '1')
                        .mapToObj(r -> (char) r)
                        .map(r -> rangeClosed('a', 'h')
                                .mapToObj(f -> (char) f)
                                .map(f -> f + "" + r)
                                .map(position -> new InlineKeyboardButton(
                                        board.squareAt(position)
                                                .piece()
                                                .map(Piece::toString)
                                                .map(String::toLowerCase)
                                                .map(properties::get)
                                                .map(Object::toString)
                                                .orElseGet(() -> board.squareAt(position).color() == LIGHT
                                                        ? properties.get("sl").toString()
                                                        : properties.get("sd").toString()))
                                        .setCallbackData(position))
                                .collect(toList()))
                        .collect(toList()));
    }

    public String color(Piece.Color color) {
        return color == Piece.Color.WHITE ? properties.getProperty("sl") : properties.getProperty("sd");
    }
}
