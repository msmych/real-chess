package matvey.realchess.telegram;

import matvey.realchess.Board;
import matvey.realchess.piece.Piece;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;
import static matvey.realchess.Square.Color.LIGHT;
import static matvey.realchess.piece.Piece.Color.BLACK;
import static matvey.realchess.piece.Piece.Color.WHITE;

public final class TelegramChessProps {

    private final Properties properties = new Properties();

    public TelegramChessProps(String file) {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InlineKeyboardMarkup chess(Board board, int gameId, String startMove) {
        return board.currentMove() == WHITE
               ? whiteChess(board, gameId, startMove)
               : blackChess(board, gameId, startMove);
    }

    public InlineKeyboardMarkup chessOpposite(Board board, int gameId, String startMove) {
        return board.currentMove() == BLACK
               ? whiteChess(board, gameId, startMove)
               : blackChess(board, gameId, startMove);
    }

    public InlineKeyboardMarkup whiteChess(Board board, int gameId, String startMove) {
        return chess(board, rangeClosed(1, 8), () -> rangeClosed('a', 'h'), gameId, startMove);
    }

    public InlineKeyboardMarkup whiteChess(Board board, int gameId) {
        return whiteChess(board, gameId, "");
    }

    public InlineKeyboardMarkup blackChess(Board board, int gameId, String startMove) {
        return chess(board, range(0, 8).map(i -> 8 - i), () -> range(0, 8).map(i -> 'h' - i), gameId, startMove);
    }

    public InlineKeyboardMarkup blackChess(Board board, int gameId) {
        return blackChess(board, gameId, "");
    }

    private InlineKeyboardMarkup chess(Board board, IntStream ranks, Supplier<IntStream> files, int gameId, String startMove) {
        return new InlineKeyboardMarkup(ranks
                .map(i -> 8 - i + '1')
                .mapToObj(r -> (char) r)
                .map(r -> files.get().mapToObj(f -> (char) f)
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
                                .setCallbackData(gameId + ":" + startMove + position))
                        .collect(toList()))
                .collect(toList()));
    }

    public String color(Piece.Color color) {
        return color == WHITE ? properties.getProperty("sl") : properties.getProperty("sd");
    }
}
