package matvey.realchess.piece;

import matvey.realchess.Board;
import matvey.realchess.Move;
import matvey.realchess.Square;

import java.util.Optional;

import static java.lang.Math.abs;
import static java.util.Optional.empty;
import static matvey.realchess.Move.*;
import static matvey.realchess.Square.square;
import static matvey.realchess.piece.Piece.Color.*;
import static matvey.realchess.piece.Piece.Color.WHITE;

public final class King extends Piece {

    private King(Color color) {
        super(color);
    }

    public static King kb() {
        return new King(BLACK);
    }

    public static King kw() {
        return new King(WHITE);
    }

    @Override
    public Optional<Move> pieceMove(Board board, Square start, Square end) {
        if (abs(end.file() - start.file()) <= 1 && abs(end.rank() - start.rank()) <= 1) {
            return end.piece()
                    .map(piece -> eat(start, end, piece))
                    .or(() -> Optional.of(basicMove(start, end)));
        }
        if (canCastle(board, start, end)) {
            return Optional.of(castling(start, end));
        }
        return empty();
    }

    private boolean canCastle(Board board, Square start, Square end) {
        if (start.rank() != end.rank() || abs(end.file() - start.file()) != 2 ||
                start.pieceInfo().filter(Square.PieceInfo::initial).isEmpty()) {
            return false;
        }
        var rook = board.squareAt(rookPositionByKingTarget(end));
        return rook.pieceInfo().filter(Square.PieceInfo::initial).isPresent() &&
                canMoveAlongLine(board, start, rook) &&
                notInCheck(board, start, end);
    }

    private boolean notInCheck(Board board, Square start, Square end) {
        for (var f = start.file(); abs(end.file() - f) > 0; f += Integer.compare(end.file(), start.file())) {
            var square = square(f + "" + start.rank()).endMove(start.piece().orElseThrow());
            if (square.inCheck(board.set(square(start.position())).set(square))) {
                return false;
            }
        }
        return true;
    }

    private String rookPositionByKingTarget(Square target) {
        return switch (target.position()) {
            case "g1" -> "h1";
            case "c1" -> "a1";
            case "g8" -> "h8";
            case "c8" -> "a8";
            default -> throw new IllegalArgumentException();
        };
    }

    @Override
    public String toString() {
        return "K" + color;
    }
}
