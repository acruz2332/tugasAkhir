package com.proyek.akhir.service.mapper;

import com.proyek.akhir.domain.History;
import com.proyek.akhir.domain.User;
import com.proyek.akhir.service.dto.HistoryDTO;
import com.proyek.akhir.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link History} and its DTO {@link HistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoryMapper extends EntityMapper<HistoryDTO, History> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    HistoryDTO toDto(History s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
