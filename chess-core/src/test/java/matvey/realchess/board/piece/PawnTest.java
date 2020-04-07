package matvey.realchess.board.piece;

import org.junit.jupiter.api.Test;

import static matvey.realchess.board.Board.emptyBoard;
import static matvey.realchess.board.Board.initialBoard;
import static matvey.realchess.board.Move.*;
import static matvey.realchess.board.Square.square;
import static matvey.realchess.board.piece.Pawn.pb;
import static matvey.realchess.board.piece.Pawn.pw;
import static org.assertj.core.api.Assertions.assertThat;

class PawnTest {

    @Test
    void white_pawn_should_move_b2b3() {
        var start = square("b2", "Pw");
        var end = square("b3");

        var move = pw().doMove(initialBoard(), start, end);

        assertThat(move).hasValue(basicMove(start, end));
    }

    @Test
    void white_pawn_should_not_move_c2d2() {
        var start = square("c2", "Pw");
        var end = square("d2");

        var move = pw().move(initialBoard(), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void white_pawn_should_not_move_c2c1() {
        var start = square("c2", "Pw");
        var end = square("c1");

        var move = pw().move(initialBoard(), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void white_pawn_should_move_d5d6() {
        var start = square("d5", "Pw");
        var end = square("d6");

        var move = pw().move(initialBoard(), start, end);

        assertThat(move).hasValue(basicMove(start, end));
    }

    @Test
    void white_pawn_should_not_move_d2d3_if_black_bishop_on_d3() {
        var start = square("d2", "Pw");
        var end = square("d3", "Bb");

        var move = pw().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void white_pawn_should_not_move_e2e3_if_white_knight_on_e3() {
        var start = square("e2", "Pw");
        var end = square("e3", "Nw");

        var move = pw().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void black_pawn_should_move_f7f6() {
        var start = square("f7", "Pb");
        var end = square("f6");

        var move = pb().move(initialBoard(), start, end);

        assertThat(move).hasValue(basicMove(start, end));
    }

    @Test
    void white_pawn_should_move_h2h4() {
        var start = square("h2", "Pw");
        var end = square("h4");

        var move = pw().move(initialBoard(), start, end);

        assertThat(move).hasValue(basicMove(start, end));
    }

    @Test
    void white_pawn_should_not_move_h4h6() {
        var start = square("h4").endMove(pw());
        var end = square("h6");

        var move = pw().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void white_pawn_should_not_move_a2a4_if_black_rook_on_a3() {
        var start = square("a2", "Pw");
        var rook = square("a3", "Rb");
        var end = square("a4");

        var move = pw().move(emptyBoard().set(rook).set(start).set(end), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void white_pawn_should_eat_f4e5() {
        var start = square("f4", "Pw");
        var end = square("e5", "Pb");

        var move = pw().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).hasValue(eat(start, end, end.piece()));
    }

    @Test
    void white_pawn_should_not_eat_white_rook_a3b4() {
        var start = square("a3", "Pw");
        var end = square("b4", "Rw");

        var move = pw().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void d5e6_white_pawn_should_eat_black_pawn_en_passant() {
        var start = square("d5", "Pw");
        var passant = square("e5").endMove(pb());
        var end = square("e6");

        var move = pw().move(emptyBoard().set(start).passant(passant), start, end);

        assertThat(move).hasValue(enPassant(start, end, passant.piece().orElseThrow()));
    }

    @Test
    void black_pawn_should_eat_white_knight_a4b3() {
        var start = square("a4", "Pb");
        var end = square("b3", "Nw");

        var move = pb().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).hasValue(eat(start, end, end.piece()));
    }

    @Test
    void f4g3_black_pawn_should_eat_white_pawn_en_passant() {
        var start = square("f4", "Pb");
        var passant = square("g4").endMove(pw());
        var end = square("g3");

        var move = pb().move(emptyBoard().set(start).passant(passant), start, end);

        assertThat(move).hasValue(enPassant(start, end, passant.piece().orElseThrow()));
    }

    @Test
    void b7c6_black_pawn_should_not_eat_en_passant_black_pawn_on_c5() {
        var start = square("b7", "Pb");
        var passant = square("c5", "Pb");
        var end = square("c6");

        var move = pb().move(emptyBoard().set(start).passant(passant), start, end);

        assertThat(move).isEmpty();
    }

}