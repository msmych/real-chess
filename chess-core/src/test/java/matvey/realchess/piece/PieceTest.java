package matvey.realchess.piece;

import org.junit.jupiter.api.Test;

import static matvey.realchess.Board.emptyBoard;
import static matvey.realchess.Board.initialBoard;
import static matvey.realchess.Square.square;
import static matvey.realchess.piece.Bishop.bw;
import static matvey.realchess.piece.King.kb;
import static matvey.realchess.piece.King.kw;
import static matvey.realchess.piece.Pawn.pb;
import static matvey.realchess.piece.Pawn.pw;
import static matvey.realchess.piece.Rook.rw;
import static org.assertj.core.api.Assertions.assertThat;

class PieceTest {

    @Test
    void b2b2_white_pawn_should_not_move() {
        var start = square("b2", "Pw");

        var move = pw().move(initialBoard(), start, start);

        assertThat(move).isEmpty();
    }

    @Test
    void d7d6_black_pawn_should_not_move_if_white_bishop_on_b5() {
        var start = square("d7", "Pb");
        var king = square("e8", "Kb");
        var bishop = square("b5").endMove(bw());
        var end = square("d6");

        var move = pb().move(emptyBoard().set(start).set(king).set(bishop), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void d5c6_white_pawn_should_not_eat_en_passant_if_white_king_on_a3_and_black_bishop_on_f8() {
        var start = square("d5").endMove(pw());
        var passant = square("c5").endMove(pb());
        var king = square("a3").endMove(kw());
        var bishop = square("f8", "Bb");
        var end = square("c6");

        var move = pw().move(emptyBoard().set(start).passant(passant).set(king).set(bishop), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void e8c8_black_king_should_castle_if_white_rook_on_c3() {
        var start = square("e8", "Kb");
        var blackRook = square("a8", "Rb");
        var whiteRook = square("c3").endMove(rw());
        var end = square("c8");

        var move = kb().move(emptyBoard().set(start).set(blackRook).set(whiteRook), start, end);

        assertThat(move).isEmpty();
    }
}