package ch.uzh.ifi.seal.soprafs16.model.repositories;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("gameRepository")
public interface GameRepository extends CrudRepository<Game, Long> {
}
