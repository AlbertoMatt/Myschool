package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Alunno;
import com.mycompany.myapp.repository.AlunnoRepository;
import com.mycompany.myapp.service.AlunnoService;
import com.mycompany.myapp.service.dto.AlunnoDTO;
import com.mycompany.myapp.service.mapper.AlunnoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Alunno}.
 */
@Service
@Transactional
public class AlunnoServiceImpl implements AlunnoService {

    private final Logger log = LoggerFactory.getLogger(AlunnoServiceImpl.class);

    private final AlunnoRepository alunnoRepository;

    private final AlunnoMapper alunnoMapper;

    public AlunnoServiceImpl(AlunnoRepository alunnoRepository, AlunnoMapper alunnoMapper) {
        this.alunnoRepository = alunnoRepository;
        this.alunnoMapper = alunnoMapper;
    }

    @Override
    public AlunnoDTO save(AlunnoDTO alunnoDTO) {
        log.debug("Request to save Alunno : {}", alunnoDTO);
        Alunno alunno = alunnoMapper.toEntity(alunnoDTO);
        alunno = alunnoRepository.save(alunno);
        return alunnoMapper.toDto(alunno);
    }

    @Override
    public AlunnoDTO update(AlunnoDTO alunnoDTO) {
        log.debug("Request to update Alunno : {}", alunnoDTO);
        Alunno alunno = alunnoMapper.toEntity(alunnoDTO);
        alunno = alunnoRepository.save(alunno);
        return alunnoMapper.toDto(alunno);
    }

    @Override
    public Optional<AlunnoDTO> partialUpdate(AlunnoDTO alunnoDTO) {
        log.debug("Request to partially update Alunno : {}", alunnoDTO);

        return alunnoRepository
            .findById(alunnoDTO.getId())
            .map(existingAlunno -> {
                alunnoMapper.partialUpdate(existingAlunno, alunnoDTO);

                return existingAlunno;
            })
            .map(alunnoRepository::save)
            .map(alunnoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlunnoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Alunnos");
        return alunnoRepository.findAll(pageable).map(alunnoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlunnoDTO> findOne(Long id) {
        log.debug("Request to get Alunno : {}", id);
        return alunnoRepository.findById(id).map(alunnoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Alunno : {}", id);
        alunnoRepository.deleteById(id);
    }
}
