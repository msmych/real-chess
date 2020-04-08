package matvey.realchess.board.piece;

import org.junit.jupiter.api.Test;

import static matvey.realchess.board.Board.emptyBoard;
import static matvey.realchess.board.Board.initialBoard;
import static matvey.realchess.board.Square.square;
import static matvey.realchess.board.piece.Bishop.bw;
import static matvey.realchess.board.piece.Pawn.pb;
import static matvey.realchess.board.piece.Pawn.pw;
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
}