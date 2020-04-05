package matvey.realchess.board.piece;

import matvey.realchess.board.Board;
import matvey.realchess.board.Move;
import matvey.realchess.board.Square;

import java.util.Optional;

import static java.lang.Math.abs;
import static java.util.Optional.empty;
import static matvey.realchess.board.Move.basicMove;
import static matvey.realchess.board.Move.eat;
import static matvey.realchess.board.piece.Piece.Color.BLACK;
import static matvey.realchess.board.piece.Piece.Color.WHITE;

public final class Pawn extends Piece {

    public static Pawn Pb = new Pawn(BLACK);
    public static Pawn Pw = new Pawn(WHITE);

    private Pawn(Color color) {
        super(color);
    }

    @Override
    Optional<Move> doMove(Board board, Square start, Square end) {
        if (start.file() != end.file()) {
            if (canEat(start, end)) {
                return Optional.of(eat(start, end, end.piece()));
            }
            if (canEatEnPassant(board, start, end)) {
                return Optional.of(eat(start, end, board.passant().flatMap(Square::piece)));
            }
            return empty();
        }
        return switch (rankDistance(start, end)) {
            case 1 -> advanceOne(start, end);
            case 2 -> advanceTwo(board, start, end);
            default -> empty();
        };
    }

    private boolean canEat(Square start, Square end) {
        return abs(end.file() - start.file()) == 1 &&
                rankDistance(start, end) == 1 &&
                end.piece().isPresent();
    }

    private boolean canEatEnPassant(Board board, Square start, Square end) {
        return board.passant()
                .map(passant -> passant.rank() == start.rank() && passant.file() == end.file())
                .orElse(false);
    }

    private Optional<Move> advanceOne(Square start, Square end) {
        if (end.piece().isPresent()) {
            return empty();
        }
        return Optional.of(basicMove(start, end));
    }

    private Optional<Move> advanceTwo(Board board, Square start, Square end) {
        if (!isInitial(start) || pieceOnTheWay(board, start, end) || end.piece().isPresent()) {
            return empty();
        }
        return Optional.of(basicMove(start, end));
    }

    private boolean isInitial(Square square) {
        return switch (color) {
            case WHITE -> square.rank() == '2';
            case BLACK -> square.rank() == '7';
        };
    }

    private boolean pieceOnTheWay(Board board, Square start, Square end) {
        return board.squareAt(start.file() + "" + ((char) ((end.rank() + start.rank()) / 2)))
                .piece().isPresent();
    }
}
