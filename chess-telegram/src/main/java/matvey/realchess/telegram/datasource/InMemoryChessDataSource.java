package matvey.realchess.telegram.datasource;

import matvey.realchess.telegram.game.Game;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InMemoryChessDataSource implements ChessDataSource {

    private final Map<Integer, Game> games = new ConcurrentHashMap<>();

    @Override
    public Optional<Game> game(int id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public Optional<Game> save(int id, Game game) {
        return Optional.ofNullable(games.putIfAbsent(id, game));
    }

    @Override
    public Optional<Game> update(int id, Function<Game, Game> modification) {
        return Optional.ofNullable(games.computeIfPresent(id, (key, game) -> modification.apply(game)));
    }
}
