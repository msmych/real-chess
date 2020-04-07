package matvey.realchess.board;

import matvey.realchess.board.piece.*;

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
        return (file - 'a' + rank - '1') % 2 == 0 ? LIGHT : DARK;
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

    public Square endMove(Piece piece) {
        return new Square(file, rank, color, Optional.of(new PieceInfo(piece, false)));
    }

    enum Color {
        LIGHT, DARK
    }
}
