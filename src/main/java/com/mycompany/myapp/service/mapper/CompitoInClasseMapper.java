package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Alunno;
import com.mycompany.myapp.domain.CompitoInClasse;
import com.mycompany.myapp.service.dto.AlunnoDTO;
import com.mycompany.myapp.service.dto.CompitoInClasseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompitoInClasse} and its DTO {@link CompitoInClasseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompitoInClasseMapper extends EntityMapper<CompitoInClasseDTO, CompitoInClasse> {
    @Mapping(target = "alunnoDiRiferimento", source = "alunnoDiRiferimento", qualifiedByName = "alunnoId")
    CompitoInClasseDTO toDto(CompitoInClasse s);

    @Named("alunnoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlunnoDTO toDtoAlunnoId(Alunno alunno);
}
