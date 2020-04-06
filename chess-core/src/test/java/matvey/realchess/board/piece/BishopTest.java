package matvey.realchess.board.piece;

import matvey.realchess.board.Square;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static matvey.realchess.board.Board.emptyBoard;
import static matvey.realchess.board.Board.initialBoard;
import static matvey.realchess.board.Move.eat;
import static matvey.realchess.board.Move.basicMove;
import static matvey.realchess.board.Square.square;
import static matvey.realchess.board.piece.Bishop.bb;
import static matvey.realchess.board.piece.Bishop.bw;
import static org.assertj.core.api.Assertions.assertThat;

class BishopTest {

    @Test
    void d5_white_bishop_should_move_to_any_of_g8_e4_a2_b7() {
        var start = square("d5", "Bw");

        Stream.of("g8", "e4", "a2", "b7")
                .map(Square::square)
                .forEach(end -> assertThat(bw().move(emptyBoard().set(start), start, end))
                        .hasValue(basicMove(start, end)));
    }

    @Test
    void f4_black_bishop_should_not_move_to_any_of_g4_e8_d3() {
        var start = square("f4", "Bb");

        Stream.of("g4", "e8", "d3")
                .map(Square::square)
                .forEach(end -> assertThat(bb().move(emptyBoard().set(start), start, end))
                        .isEmpty());
    }

    @Test
    void b5d7_white_bishop_should_eat_black_pawn() {
        var start = square("b5", "Bw");
        var end = square("d7", "Pb");

        var move = bw().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).hasValue(eat(start, end, end.piece()));
    }

    @Test
    void c8g4_black_bishop_should_not_move_if_piece_on_d7() {
        var start = square("c8", "Bb");
        var end = square("g4");

        var move = bb().move(initialBoard(), start, end);

        assertThat(move).isEmpty();
    }
}