package matvey.realchess.board;

import matvey.realchess.board.piece.*;

import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.board.Square.Color.DARK;
import static matvey.realchess.board.Square.Color.LIGHT;
import static matvey.realchess.board.piece.Bishop.Bw;
import static matvey.realchess.board.piece.Bishop.Bb;
import static matvey.realchess.board.piece.King.Kw;
import static matvey.realchess.board.piece.King.Kb;
import static matvey.realchess.board.piece.Knight.Nw;
import static matvey.realchess.board.piece.Knight.Nb;
import static matvey.realchess.board.piece.Pawn.Pw;
import static matvey.realchess.board.piece.Pawn.Pb;
import static matvey.realchess.board.piece.Queen.Qw;
import static matvey.realchess.board.piece.Queen.Qb;
import static matvey.realchess.board.piece.Rook.Rw;
import static matvey.realchess.board.piece.Rook.Rb;

public record Square(char file,
                     char rank,
                     Color color,
                     Optional<Piece>piece) {

    public static Square square(String position) {
        return square(position, "..");
    }

    public static Square square(String position, String piece) {
        return square(position.charAt(0), position.charAt(1), piece);
    }

    public static Square square(char file, char rank, String piece) {
        return new Square(file, rank, color(file, rank), piece(piece));
    }

    private static Color color(char file, char rank) {
        return (file - 'a' + rank - '1') % 2 == 0 ? LIGHT : DARK;
    }

    private static Optional<Piece> piece(String symbol) {
        return switch (symbol) {
            case "Kw" -> Optional.of(Kw);
            case "Kb" -> Optional.of(Kb);
            case "Qw" -> Optional.of(Qw);
            case "Qb" -> Optional.of(Qb);
            case "Rb" -> Optional.of(Rb);
            case "Rw" -> Optional.of(Rw);
            case "Bb" -> Optional.of(Bb);
            case "Bw" -> Optional.of(Bw);
            case "Nb" -> Optional.of(Nb);
            case "Nw" -> Optional.of(Nw);
            case "Pb" -> Optional.of(Pb);
            case "Pw" -> Optional.of(Pw);
            default -> empty();
        };
    }

    enum Color {
        LIGHT, DARK
    }
}
