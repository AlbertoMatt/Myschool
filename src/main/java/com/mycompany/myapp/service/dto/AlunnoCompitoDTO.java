package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AlunnoCompito} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlunnoCompitoDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "10")
    private Double risultatoNumerico;

    private ZonedDateTime dataRestituizione;

    private AlunnoDTO alunno;

    private CompitoInClasseDTO compito;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRisultatoNumerico() {
        return risultatoNumerico;
    }

    public void setRisultatoNumerico(Double risultatoNumerico) {
        this.risultatoNumerico = risultatoNumerico;
    }

    public ZonedDateTime getDataRestituizione() {
        return dataRestituizione;
    }

    public void setDataRestituizione(ZonedDateTime dataRestituizione) {
        this.dataRestituizione = dataRestituizione;
    }

    public AlunnoDTO getAlunno() {
        return alunno;
    }

    public void setAlunno(AlunnoDTO alunno) {
        this.alunno = alunno;
    }

    public CompitoInClasseDTO getCompito() {
        return compito;
    }

    public void setCompito(CompitoInClasseDTO compito) {
        this.compito = compito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlunnoCompitoDTO)) {
            return false;
        }

        AlunnoCompitoDTO alunnoCompitoDTO = (AlunnoCompitoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alunnoCompitoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlunnoCompitoDTO{" +
            "id=" + getId() +
            ", risultatoNumerico=" + getRisultatoNumerico() +
            ", dataRestituizione='" + getDataRestituizione() + "'" +
            ", alunno=" + getAlunno() +
            ", compito=" + getCompito() +
            "}";
    }
}
