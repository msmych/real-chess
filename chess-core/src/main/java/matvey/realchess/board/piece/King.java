package matvey.realchess.board.piece;

import matvey.realchess.board.Board;
import matvey.realchess.board.Move;
import matvey.realchess.board.Square;

import java.util.Optional;

import static java.lang.Math.abs;
import static java.util.Optional.empty;
import static matvey.realchess.board.Move.basicMove;
import static matvey.realchess.board.piece.Piece.Color.*;
import static matvey.realchess.board.piece.Piece.Color.WHITE;

public final class King extends Piece {

    public static King Kw = new King(WHITE);
    public static King Kb = new King(BLACK);

    private King(Color color) {
        super(color);
    }

    @Override
    Optional<Move> doMove(Board board, Square start, Square end) {
        if (abs(end.file() - start.file()) <= 1 && abs(end.rank() - start.rank()) <= 1) {
            return end.piece()
                    .map(piece -> Move.eat(start, end, piece))
                    .or(() -> Optional.of(basicMove(start, end)));
        }
        return empty();
    }
}
