package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Alunno;
import com.mycompany.myapp.domain.AlunnoCompito;
import com.mycompany.myapp.domain.CompitoInClasse;
import com.mycompany.myapp.service.dto.AlunnoCompitoDTO;
import com.mycompany.myapp.service.dto.AlunnoDTO;
import com.mycompany.myapp.service.dto.CompitoInClasseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlunnoCompito} and its DTO {@link AlunnoCompitoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlunnoCompitoMapper extends EntityMapper<AlunnoCompitoDTO, AlunnoCompito> {
    @Mapping(target = "alunno", source = "alunno", qualifiedByName = "alunnoId")
    @Mapping(target = "compito", source = "compito", qualifiedByName = "compitoInClasseId")
    AlunnoCompitoDTO toDto(AlunnoCompito s);

    @Named("alunnoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlunnoDTO toDtoAlunnoId(Alunno alunno);

    @Named("compitoInClasseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompitoInClasseDTO toDtoCompitoInClasseId(CompitoInClasse compitoInClasse);
}
