package ee.aleksale.betgame.common.repository;

import ee.aleksale.betgame.auth.model.domain.PlayerEntity;
import ee.aleksale.betgame.common.BaseRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PlayerRepositoryTest extends BaseRepositoryTest {

    private static final double DEFAULT_AMOUNT = 50.0;

    @Autowired
    private PlayerRepository playerRepository;

    private PlayerEntity playerEntity;

    @BeforeEach
    void setup() {
        playerEntity = createPlayerEntity(RandomStringUtils.random(5));
    }

    @Test
    void save() {
        var insertedEntity = playerRepository.save(playerEntity);

        assertNotNull(insertedEntity);
    }

    @Test
    void existsByIdAnUsername() {
        var insertedEntity = playerRepository.save(playerEntity);

        var result = playerRepository.existsByIdAndUsername(insertedEntity.getId(), insertedEntity.getUsername());

        assertTrue(result);
    }

    @Test
    void existsByUsername() {
        var insertedEntity = playerRepository.save(playerEntity);

        var result = playerRepository.existsByUsername(insertedEntity.getUsername());

        assertTrue(result);
    }

    @Test
    void findByUsername() {
        var insertedEntity = playerRepository.save(playerEntity);

        var result = playerRepository.findByUsername(insertedEntity.getUsername());

        assertTrue(result.isPresent());
        assertEquals(insertedEntity.getUsername(), result.get().getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void increatePlayerAmount() {
        var insertedEntity = playerRepository.save(playerEntity);
        playerRepository.flush();

        var updatedSize = playerRepository.increasePlayerAmount(insertedEntity.getUsername(), 10.0);

        assertEquals(1, updatedSize);
    }

    @Test
    @Transactional
    void decreasePlayerAmount() {
        var insertedEntity = playerRepository.save(playerEntity);

        var updatedSize = playerRepository.decreasePlayerAmount(insertedEntity.getUsername(), 10.0);

        assertEquals(1, updatedSize);
    }

    private PlayerEntity createPlayerEntity(String username) {
        var entity = new PlayerEntity();
        entity.setUsername(username);
        entity.setTotalAmount(DEFAULT_AMOUNT);
        return entity;
    }
}
