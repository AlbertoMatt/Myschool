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
    private Materia materia;

    @NotNull
    private LocalDate data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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
            ", materia='" + getMateria() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
