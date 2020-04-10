package matvey.realchess.board;

import matvey.realchess.board.piece.Piece;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.board.Square.Color.DARK;
import static matvey.realchess.board.Square.Color.LIGHT;
import static matvey.realchess.board.piece.Bishop.bb;
import static matvey.realchess.board.piece.Bishop.bw;
import static matvey.realchess.board.piece.King.kb;
import static matvey.realchess.board.piece.King.kw;
import static matvey.realchess.board.piece.Knight.nb;
import static matvey.realchess.board.piece.Knight.nw;
import static matvey.realchess.board.piece.Pawn.pb;
import static matvey.realchess.board.piece.Pawn.pw;
import static matvey.realchess.board.piece.Queen.qb;
import static matvey.realchess.board.piece.Queen.qw;
import static matvey.realchess.board.piece.Rook.rb;
import static matvey.realchess.board.piece.Rook.rw;

public record Square(char file,
                     char rank,
                     Color color,
                     Optional<PieceInfo>pieceInfo) {

    public record PieceInfo(Piece piece, boolean initial) {}

    public static Square square(String position) {
        return square(position, "..");
    }

    public static Square square(String position, String piece) {
        return square(position.charAt(0), position.charAt(1), piece);
    }

    public static Square square(char file, char rank, String piece) {
        return new Square(file, rank, color(file, rank), piece(piece).map(p -> new PieceInfo(p, true)));
    }

    private static Color color(char file, char rank) {
        return (file - 'a' + rank - '1') % 2 == 1 ? LIGHT : DARK;
    }

    private static Optional<Piece> piece(String symbol) {
        return switch (symbol.toLowerCase()) {
            case "kw" -> Optional.of(kw());
            case "kb" -> Optional.of(kb());
            case "qw" -> Optional.of(qw());
            case "qb" -> Optional.of(qb());
            case "rb" -> Optional.of(rb());
            case "rw" -> Optional.of(rw());
            case "bb" -> Optional.of(bb());
            case "bw" -> Optional.of(bw());
            case "nb" -> Optional.of(nb());
            case "nw" -> Optional.of(nw());
            case "pb" -> Optional.of(pb());
            case "pw" -> Optional.of(pw());
            default -> empty();
        };
    }

    public String position() {
        return file + "" + rank;
    }

    public Optional<Piece> piece() {
        return pieceInfo.map(PieceInfo::piece);
    }

    public Optional<Move> move(Board board, Square end) {
        return piece()
                .filter(piece -> board.currentMove() == piece.color())
                .flatMap(piece -> piece.move(board, this, end));
    }

    public Square endMove(Piece piece) {
        return new Square(file, rank, color, Optional.of(new PieceInfo(piece, false)));
    }

    public boolean inCheck(Board board) {
        return board.squares().values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(square -> square.piece()
                        .map(piece -> piece.color() != piece().map(Piece::color).orElseThrow())
                        .orElse(false))
                .anyMatch(square -> square.piece()
                        .filter(piece -> piece.pieceMove(board, square, this).isPresent())
                        .isPresent());
    }

    public String rookPositionForCastling() {
        return switch (position()) {
            case "g1" -> "h1";
            case "c1" -> "a1";
            case "g8" -> "h8";
            case "c8" -> "a8";
            default -> throw new IllegalArgumentException();
        };
    }

    public String rookTargetForCastling() {
        return switch (position()) {
            case "c1" -> "d1";
            case "g1" -> "f1";
            case "c8" -> "d8";
            case "g8" -> "f8";
            default -> throw new IllegalArgumentException();
        };
    }

    enum Color {
        LIGHT, DARK;

        @Override
        public String toString() {
            return this == LIGHT ? "%%" : "..";
        }
    }

    @Override
    public String toString() {
        return piece().map(Piece::toString).orElse(color.toString());
    }
}
