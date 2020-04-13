package matvey.realchess.console;

import matvey.realchess.Board;
import matvey.realchess.piece.Piece;

import java.util.Scanner;

import static matvey.realchess.Board.initialBoard;
import static matvey.realchess.Square.Color.LIGHT;

public class ConsoleChess {

    public static void main(String[] args) {
        var props = new Props("chess.properties");
        var board = initialBoard();
        var scanner = new Scanner(System.in);
        while (true) {
            System.out.println(serialize(board, props));
            var move = scanner.nextLine();
            var moveResult = board.move(move);
            if (moveResult.isPresent()) {
                if (moveResult.get().winner().isPresent()) {
                    System.out.println(moveResult.get().winner() + " WINS");
                    break;
                }
                board = moveResult.get().board();
            }
        }
    }

    private static String serialize(Board board, Props props) {
        var sb = new StringBuilder("  a  b  c  d  e  f  g  h  \n");
        for (var r = '8'; r >= '1'; r--) {
            sb.append(r);
            for (var f = 'a'; f <= 'h'; f++) {
                var square = board.squareAt(f + "" + r);
                sb.append(' ')
                        .append(square.piece()
                                .map(Piece::toString)
                                .map(String::toLowerCase)
                                .map(props::get)
                                .orElseGet(() -> props.get(square.color() == LIGHT ? "sl" : "sd")))
                        .append(' ');
            }
            sb.append(r).append('\n');
        }
        return sb.append("  a  b  c  d  e  f  g  h  ")
                .append('\n')
                .append("Current move: ").append(board.currentMove())
                .toString();
    }

}
