package ee.aleksale.betgame.common.repository;

import ee.aleksale.betgame.auth.model.domain.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    boolean existsByIdAndUsername(Long id, String username);

    boolean existsByUsername(String username);

    Optional<PlayerEntity> findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE game.players " +
            "SET total_amount = total_amount + :value " +
            "WHERE username = :username", nativeQuery = true)
    void increasePlayerAmount(@Param("username") String username, @Param("value") double value);

    @Modifying
    @Query(value = "UPDATE game.players " +
            "SET total_amount = total_amount - :value " +
            "WHERE username = :username", nativeQuery = true)
    void decreasePlayerAmount(@Param("username") String username, @Param("value") double value);
}
