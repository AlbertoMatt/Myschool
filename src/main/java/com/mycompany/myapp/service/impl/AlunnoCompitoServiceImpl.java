package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AlunnoCompito;
import com.mycompany.myapp.repository.AlunnoCompitoRepository;
import com.mycompany.myapp.service.AlunnoCompitoService;
import com.mycompany.myapp.service.dto.AlunnoCompitoDTO;
import com.mycompany.myapp.service.mapper.AlunnoCompitoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AlunnoCompito}.
 */
@Service
@Transactional
public class AlunnoCompitoServiceImpl implements AlunnoCompitoService {

    private final Logger log = LoggerFactory.getLogger(AlunnoCompitoServiceImpl.class);

    private final AlunnoCompitoRepository alunnoCompitoRepository;

    private final AlunnoCompitoMapper alunnoCompitoMapper;

    public AlunnoCompitoServiceImpl(AlunnoCompitoRepository alunnoCompitoRepository, AlunnoCompitoMapper alunnoCompitoMapper) {
        this.alunnoCompitoRepository = alunnoCompitoRepository;
        this.alunnoCompitoMapper = alunnoCompitoMapper;
    }

    @Override
    public AlunnoCompitoDTO save(AlunnoCompitoDTO alunnoCompitoDTO) {
        log.debug("Request to save AlunnoCompito : {}", alunnoCompitoDTO);
        AlunnoCompito alunnoCompito = alunnoCompitoMapper.toEntity(alunnoCompitoDTO);
        alunnoCompito = alunnoCompitoRepository.save(alunnoCompito);
        return alunnoCompitoMapper.toDto(alunnoCompito);
    }

    @Override
    public AlunnoCompitoDTO update(AlunnoCompitoDTO alunnoCompitoDTO) {
        log.debug("Request to update AlunnoCompito : {}", alunnoCompitoDTO);
        AlunnoCompito alunnoCompito = alunnoCompitoMapper.toEntity(alunnoCompitoDTO);
        alunnoCompito = alunnoCompitoRepository.save(alunnoCompito);
        return alunnoCompitoMapper.toDto(alunnoCompito);
    }

    @Override
    public Optional<AlunnoCompitoDTO> partialUpdate(AlunnoCompitoDTO alunnoCompitoDTO) {
        log.debug("Request to partially update AlunnoCompito : {}", alunnoCompitoDTO);

        return alunnoCompitoRepository
            .findById(alunnoCompitoDTO.getId())
            .map(existingAlunnoCompito -> {
                alunnoCompitoMapper.partialUpdate(existingAlunnoCompito, alunnoCompitoDTO);

                return existingAlunnoCompito;
            })
            .map(alunnoCompitoRepository::save)
            .map(alunnoCompitoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlunnoCompitoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AlunnoCompitos");
        return alunnoCompitoRepository.findAll(pageable).map(alunnoCompitoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlunnoCompitoDTO> findOne(Long id) {
        log.debug("Request to get AlunnoCompito : {}", id);
        return alunnoCompitoRepository.findById(id).map(alunnoCompitoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AlunnoCompito : {}", id);
        alunnoCompitoRepository.deleteById(id);
    }
}
