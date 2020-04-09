package matvey.realchess;

import matvey.realchess.board.Board;

import static matvey.realchess.board.Board.initialBoard;

public final class Chess {

    private Board board = initialBoard();

    public static void main(String[] args) {
        System.out.println(initialBoard());
    }
}
