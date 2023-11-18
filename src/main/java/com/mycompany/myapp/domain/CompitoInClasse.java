package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Materia;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CompitoInClasse.
 */
@Entity
@Table(name = "compito_in_classe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompitoInClasse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "materia", nullable = false)
    private Materia materia;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "data_restituizione")
    private ZonedDateTime dataRestituizione;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compito")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alunno", "compito" }, allowSetters = true)
    private Set<AlunnoCompito> alunnis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompitoInClasse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Materia getMateria() {
        return this.materia;
    }

    public CompitoInClasse materia(Materia materia) {
        this.setMateria(materia);
        return this;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public LocalDate getData() {
        return this.data;
    }

    public CompitoInClasse data(LocalDate data) {
        this.setData(data);
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public ZonedDateTime getDataRestituizione() {
        return this.dataRestituizione;
    }

    public CompitoInClasse dataRestituizione(ZonedDateTime dataRestituizione) {
        this.setDataRestituizione(dataRestituizione);
        return this;
    }

    public void setDataRestituizione(ZonedDateTime dataRestituizione) {
        this.dataRestituizione = dataRestituizione;
    }

    public Set<AlunnoCompito> getAlunnis() {
        return this.alunnis;
    }

    public void setAlunnis(Set<AlunnoCompito> alunnoCompitos) {
        if (this.alunnis != null) {
            this.alunnis.forEach(i -> i.setCompito(null));
        }
        if (alunnoCompitos != null) {
            alunnoCompitos.forEach(i -> i.setCompito(this));
        }
        this.alunnis = alunnoCompitos;
    }

    public CompitoInClasse alunnis(Set<AlunnoCompito> alunnoCompitos) {
        this.setAlunnis(alunnoCompitos);
        return this;
    }

    public CompitoInClasse addAlunni(AlunnoCompito alunnoCompito) {
        this.alunnis.add(alunnoCompito);
        alunnoCompito.setCompito(this);
        return this;
    }

    public CompitoInClasse removeAlunni(AlunnoCompito alunnoCompito) {
        this.alunnis.remove(alunnoCompito);
        alunnoCompito.setCompito(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompitoInClasse)) {
            return false;
        }
        return getId() != null && getId().equals(((CompitoInClasse) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompitoInClasse{" +
            "id=" + getId() +
            ", materia='" + getMateria() + "'" +
            ", data='" + getData() + "'" +
            ", dataRestituizione='" + getDataRestituizione() + "'" +
            "}";
    }
}
