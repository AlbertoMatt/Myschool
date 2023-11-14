package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CompitoInClasse;
import com.mycompany.myapp.repository.CompitoInClasseRepository;
import com.mycompany.myapp.service.CompitoInClasseService;
import com.mycompany.myapp.service.dto.CompitoInClasseDTO;
import com.mycompany.myapp.service.mapper.CompitoInClasseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.CompitoInClasse}.
 */
@Service
@Transactional
public class CompitoInClasseServiceImpl implements CompitoInClasseService {

    private final Logger log = LoggerFactory.getLogger(CompitoInClasseServiceImpl.class);

    private final CompitoInClasseRepository compitoInClasseRepository;

    private final CompitoInClasseMapper compitoInClasseMapper;

    public CompitoInClasseServiceImpl(CompitoInClasseRepository compitoInClasseRepository, CompitoInClasseMapper compitoInClasseMapper) {
        this.compitoInClasseRepository = compitoInClasseRepository;
        this.compitoInClasseMapper = compitoInClasseMapper;
    }

    @Override
    public CompitoInClasseDTO save(CompitoInClasseDTO compitoInClasseDTO) {
        log.debug("Request to save CompitoInClasse : {}", compitoInClasseDTO);
        CompitoInClasse compitoInClasse = compitoInClasseMapper.toEntity(compitoInClasseDTO);
        compitoInClasse = compitoInClasseRepository.save(compitoInClasse);
        return compitoInClasseMapper.toDto(compitoInClasse);
    }

    @Override
    public CompitoInClasseDTO update(CompitoInClasseDTO compitoInClasseDTO) {
        log.debug("Request to update CompitoInClasse : {}", compitoInClasseDTO);
        CompitoInClasse compitoInClasse = compitoInClasseMapper.toEntity(compitoInClasseDTO);
        compitoInClasse = compitoInClasseRepository.save(compitoInClasse);
        return compitoInClasseMapper.toDto(compitoInClasse);
    }

    @Override
    public Optional<CompitoInClasseDTO> partialUpdate(CompitoInClasseDTO compitoInClasseDTO) {
        log.debug("Request to partially update CompitoInClasse : {}", compitoInClasseDTO);

        return compitoInClasseRepository
            .findById(compitoInClasseDTO.getId())
            .map(existingCompitoInClasse -> {
                compitoInClasseMapper.partialUpdate(existingCompitoInClasse, compitoInClasseDTO);

                return existingCompitoInClasse;
            })
            .map(compitoInClasseRepository::save)
            .map(compitoInClasseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompitoInClasseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompitoInClasses");
        return compitoInClasseRepository.findAll(pageable).map(compitoInClasseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompitoInClasseDTO> findOne(Long id) {
        log.debug("Request to get CompitoInClasse : {}", id);
        return compitoInClasseRepository.findById(id).map(compitoInClasseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompitoInClasse : {}", id);
        compitoInClasseRepository.deleteById(id);
    }
}
