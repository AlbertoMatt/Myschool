package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Materia;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CompitoInClasse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompitoInClasseDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate data;

    @NotNull
    private Materia materia;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "10")
    private Double risultatoNumerico;

    private AlunnoDTO alunnoDiRiferimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Double getRisultatoNumerico() {
        return risultatoNumerico;
    }

    public void setRisultatoNumerico(Double risultatoNumerico) {
        this.risultatoNumerico = risultatoNumerico;
    }

    public AlunnoDTO getAlunnoDiRiferimento() {
        return alunnoDiRiferimento;
    }

    public void setAlunnoDiRiferimento(AlunnoDTO alunnoDiRiferimento) {
        this.alunnoDiRiferimento = alunnoDiRiferimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompitoInClasseDTO)) {
            return false;
        }

        CompitoInClasseDTO compitoInClasseDTO = (CompitoInClasseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compitoInClasseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompitoInClasseDTO{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", materia='" + getMateria() + "'" +
            ", risultatoNumerico=" + getRisultatoNumerico() +
            ", alunnoDiRiferimento=" + getAlunnoDiRiferimento() +
            "}";
    }
}
