package matvey.realchess.board.piece;

import matvey.realchess.board.Board;
import matvey.realchess.board.Move;
import matvey.realchess.board.Square;

import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.board.Move.basicMove;
import static matvey.realchess.board.Move.eat;
import static matvey.realchess.board.piece.Piece.Color.BLACK;
import static matvey.realchess.board.piece.Piece.Color.WHITE;

public final class Bishop extends Piece {

    private Bishop(Color color) {
        super(color);
    }

    public static Bishop bb() {
        return new Bishop(BLACK);
    }

    public static Bishop bw() {
        return new Bishop(WHITE);
    }

    @Override
    Optional<Move> doMove(Board board, Square start, Square end) {
        if (canMoveDiagonally(board, start, end)) {
            return end.piece()
                    .map(piece -> eat(start, end, piece))
                    .or(() -> Optional.of(basicMove(start, end)));
        }
        return empty();
    }

}
