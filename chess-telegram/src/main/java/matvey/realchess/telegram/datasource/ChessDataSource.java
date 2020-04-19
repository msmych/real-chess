package matvey.realchess.telegram.datasource;

import matvey.realchess.telegram.game.Game;

import java.util.Optional;
import java.util.function.Function;

public interface ChessDataSource {

    Optional<Game> game(int id);

    Optional<Game> save(int id, Game game);

    Optional<Game> update(int id, Function<Game, Game> modification);
}
