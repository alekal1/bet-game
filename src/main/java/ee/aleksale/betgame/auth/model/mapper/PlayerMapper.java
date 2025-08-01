package ee.aleksale.betgame.auth.model.mapper;

import ee.aleksale.betgame.auth.model.api.AuthRequest;
import ee.aleksale.betgame.auth.model.domain.PlayerEntity;
import ee.aleksale.betgame.auth.utils.RestConstants;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    default PlayerEntity map(AuthRequest authRequest) {
        var entity = new PlayerEntity();
        entity.setUsername(authRequest.getUsername());
        entity.setTotalAmount(RestConstants.INIT_PLAYERS_AMOUNT);

        return entity;
    }
}
