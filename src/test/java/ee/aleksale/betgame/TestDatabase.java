package ee.aleksale.betgame;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers(disabledWithoutDocker = true)
public abstract class TestDatabase {

    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("game_db")
            .withUsername("game_user")
            .withPassword("game_user_password")
            .withInitScript("db/dev/createdb.sql");

    @BeforeAll
    static void setupAll() {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl() + "&currentSchema=game");
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }
}
