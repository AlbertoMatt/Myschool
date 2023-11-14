package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Alunno;
import com.mycompany.myapp.domain.Classe;
import com.mycompany.myapp.service.dto.AlunnoDTO;
import com.mycompany.myapp.service.dto.ClasseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alunno} and its DTO {@link AlunnoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlunnoMapper extends EntityMapper<AlunnoDTO, Alunno> {
    @Mapping(target = "classeDiAppartenenza", source = "classeDiAppartenenza", qualifiedByName = "classeId")
    AlunnoDTO toDto(Alunno s);

    @Named("classeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClasseDTO toDtoClasseId(Classe classe);
}
