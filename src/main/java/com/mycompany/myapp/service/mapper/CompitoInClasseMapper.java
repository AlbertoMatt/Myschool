package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.CompitoInClasse;
import com.mycompany.myapp.service.dto.CompitoInClasseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompitoInClasse} and its DTO {@link CompitoInClasseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompitoInClasseMapper extends EntityMapper<CompitoInClasseDTO, CompitoInClasse> {}
