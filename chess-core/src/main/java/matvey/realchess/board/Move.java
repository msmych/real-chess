package matvey.realchess.board;

import matvey.realchess.board.piece.Piece;

import java.util.Optional;

import static java.util.Optional.empty;

public record Move(Square start,
                   Square end,
                   Optional<Piece>eaten) {

    public static Move basicMove(Square start, Square end) {
        return new Move(start, end, empty());
    }

    public static Move eat(Square start, Square end, Optional<Piece> eaten) {
        return new Move(start, end, eaten);
    }

    public static Move eat(Square start, Square end, Piece eaten) {
        return eat(start, end, Optional.of(eaten));
    }
}
