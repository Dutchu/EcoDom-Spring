package edu.weeia.ecodom.api.v1.mapper;

import edu.weeia.ecodom.api.v1.model.UserCreatedDto;
import edu.weeia.ecodom.api.v1.model.UserPreviewDto;
import edu.weeia.ecodom.domain.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "createdDate", target = "createdAt", qualifiedByName = "instantToLocalDateTime")
    @Mapping(source = "avatar", target = "icon", qualifiedByName = "stringFromBytes")
    UserPreviewDto mapForPreview(AppUser user);


    UserCreatedDto mapAfterCreate(AppUser user);

    @Named("stringFromBytes")
    public static String stringFromBytes(byte[] bytes) {
        return bytes == null ? null :  Base64.getEncoder().encodeToString(bytes);
    }

    @Named("instantToLocalDateTime")
    default LocalDateTime mapInstantToLocalDateTime(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
