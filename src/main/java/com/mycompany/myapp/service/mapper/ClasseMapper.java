package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Classe;
import com.mycompany.myapp.service.dto.ClasseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Classe} and its DTO {@link ClasseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClasseMapper extends EntityMapper<ClasseDTO, Classe> {}
