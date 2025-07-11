## Getting started

Build project `./gradlew clean build`

Make sure Docker is installed on your local machine.

Run `docker compose up -d` to start application and database in docker.

Open [http://localhost:9999](http://localhost:9999) with your browser to see the result.

## Connection to WebSocket

This project is divided by two main parts:

* Authorization REST API ([See swagger](http://localhost:9999/swagger-ui/index.html#/))
* WebSocket ([ws://localhost:9999/ws/bet-game](ws://localhost:9999/ws/bet-game))

To play the game first, you need to **get JWT token** that you can retrieve via POST request `/v1/auth`.

After you retrieve your token, **make sure to place it under `Authorization`
header with `Bearer ` prefix** while connecting to websocket.

Example: `Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.ey....`

### WebSocket message structure

```aiignore
{
    "number": 8,
    "amount": 50
}
```

## Configuration

The default game configuration is as follows:

* Win multiplier - **9.9**
* Round duration is seconds - **10**

To change those settings change according value, in `application.yml` or via environment in Docker.

## Simulation

You can run RTPSimulation to simulate game process, that runs in 24 threads - `./gradlew rtpSimulation`

Arguments:
* `totalRounds` - How many total rounds. (Since simulation is running on 24 threads, the minimal value is 24) - **default 1 million**
* `playersBet` - Player's bet in each round - **default 1.0**
* `winMultiplier` - Multiplies with players bet to calculate total win in round - **default 9.9**

Example of usage: `./gradlew rtpSimulation -PtotalRounds='100' -PwinMultiplier='3.14' -PplayersBet='5'`

For more details, of how this simulation works check `RTPSimulation.class` under test package.

**_This is a simulation; a player has unlimited money to bet._**

## Integration (requires Docker to run)

You can run integration test to ensure that basic game logic is working.

`./gradlew integration`

* Register a player
* Connect to a session with a retrieved JWT token
* Waiting for round start
* Make an invalid bet
* Make a valid bet
* Waiting for the round end

**It runs with a dedicated database container**, so **your local database state will not be changed** during integration

## Dependencies

This project is using the following dependencies:

* Java 17
* Gradle
* Spring boot
* WebSocket
* Testcontainers
* Postgres
* Liquidbase

For details see `dependencies.gradle`
