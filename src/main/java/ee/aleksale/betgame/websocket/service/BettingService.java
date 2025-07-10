package ee.aleksale.betgame.websocket.service;

import ee.aleksale.betgame.common.repository.PlayerRepository;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.registry.RoundBetRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BettingService {

    private final PlayerRepository playerRepository;
    private final RoundBetRegistry roundBetRegistry;

    @Transactional
    public void addAmountToPlayer(String username, double valueToAdd) {
        playerRepository.increasePlayerAmount(username, valueToAdd);
    }

    @Transactional
    public void removeAmountFromPlayer(PlayerBet playerBet) {
        playerRepository.decreasePlayerAmount(playerBet.getUsername(), playerBet.getAmount());
    }

    public List<PlayerBet> getWinBets(int winningNumber) {
        return roundBetRegistry.getAll().values().stream()
                .filter(playerBet -> playerBet.getNumber() == winningNumber)
                .toList();
    }

    public List<PlayerBet> getLoseBets(int winningNumber) {
        return roundBetRegistry.getAll().values().stream()
                .filter(playerBet -> playerBet.getNumber() != winningNumber)
                .toList();
    }
}
