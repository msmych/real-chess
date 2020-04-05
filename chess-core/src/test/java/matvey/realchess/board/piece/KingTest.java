package matvey.realchess.board.piece;

import matvey.realchess.board.Square;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static matvey.realchess.board.Board.emptyBoard;
import static matvey.realchess.board.Board.initialBoard;
import static matvey.realchess.board.Move.basicMove;
import static matvey.realchess.board.Move.eat;
import static matvey.realchess.board.Square.square;
import static matvey.realchess.board.piece.King.Kb;
import static matvey.realchess.board.piece.King.Kw;
import static org.assertj.core.api.Assertions.assertThat;

class KingTest {

    @Test
    void e3_white_king_should_move_to_any_of_e4_f4_f3_f2_e2_d2_d3_d4() {
        var start = square("e3", "Kw");

        Stream.of("e4", "f4", "f3", "f2", "e2", "d2", "d3", "d4")
                .map(Square::square)
                .forEach(end -> assertThat(Kw.move(initialBoard().set(start), start, end))
                        .hasValue(basicMove(start, end)));
    }

    @Test
    void e5_black_king_should_not_move_to_any_of_e8_f7_g7_g6() {
        var start = square("e5", "Kb");

        Stream.of("e8", "f7", "g7", "g6")
                .map(Square::square)
                .forEach(end -> assertThat(Kb.move(emptyBoard().set(start), start, end))
                        .isEmpty());
    }

    @Test
    void g4h5_white_king_should_eat_black_rook() {
        var start = square("g4", "Kw");
        var end = square("h5", "Rb");

        var move = Kw.move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).hasValue(eat(start, end, end.piece()));
    }
}