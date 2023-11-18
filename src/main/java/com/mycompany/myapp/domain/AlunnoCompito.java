package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlunnoCompito.
 */
@Entity
@Table(name = "alunno_compito")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlunnoCompito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "10")
    @Column(name = "risultato_numerico", nullable = false)
    private Double risultatoNumerico;

    @Column(name = "data_restituizione")
    private ZonedDateTime dataRestituizione;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "compitiEseguitis", "classeDiAppartenenza" }, allowSetters = true)
    private Alunno alunno;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "alunnis" }, allowSetters = true)
    private CompitoInClasse compito;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlunnoCompito id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRisultatoNumerico() {
        return this.risultatoNumerico;
    }

    public AlunnoCompito risultatoNumerico(Double risultatoNumerico) {
        this.setRisultatoNumerico(risultatoNumerico);
        return this;
    }

    public void setRisultatoNumerico(Double risultatoNumerico) {
        this.risultatoNumerico = risultatoNumerico;
    }

    public ZonedDateTime getDataRestituizione() {
        return this.dataRestituizione;
    }

    public AlunnoCompito dataRestituizione(ZonedDateTime dataRestituizione) {
        this.setDataRestituizione(dataRestituizione);
        return this;
    }

    public void setDataRestituizione(ZonedDateTime dataRestituizione) {
        this.dataRestituizione = dataRestituizione;
    }

    public Alunno getAlunno() {
        return this.alunno;
    }

    public void setAlunno(Alunno alunno) {
        this.alunno = alunno;
    }

    public AlunnoCompito alunno(Alunno alunno) {
        this.setAlunno(alunno);
        return this;
    }

    public CompitoInClasse getCompito() {
        return this.compito;
    }

    public void setCompito(CompitoInClasse compitoInClasse) {
        this.compito = compitoInClasse;
    }

    public AlunnoCompito compito(CompitoInClasse compitoInClasse) {
        this.setCompito(compitoInClasse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlunnoCompito)) {
            return false;
        }
        return getId() != null && getId().equals(((AlunnoCompito) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlunnoCompito{" +
            "id=" + getId() +
            ", risultatoNumerico=" + getRisultatoNumerico() +
            ", dataRestituizione='" + getDataRestituizione() + "'" +
            "}";
    }
}
