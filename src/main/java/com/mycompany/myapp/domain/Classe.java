package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Classe.
 */
@Entity
@Table(name = "classe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Classe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "indicazione_numerica", nullable = false)
    private Integer indicazioneNumerica;

    @NotNull
    @Size(max = 1)
    @Pattern(regexp = "^[A-E]")
    @Column(name = "sezione", length = 1, nullable = false)
    private String sezione;

    @Column(name = "note")
    private String note;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classeDiAppartenenza")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "compitiEseguitis", "classeDiAppartenenza" }, allowSetters = true)
    private Set<Alunno> alunnis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Classe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndicazioneNumerica() {
        return this.indicazioneNumerica;
    }

    public Classe indicazioneNumerica(Integer indicazioneNumerica) {
        this.setIndicazioneNumerica(indicazioneNumerica);
        return this;
    }

    public void setIndicazioneNumerica(Integer indicazioneNumerica) {
        this.indicazioneNumerica = indicazioneNumerica;
    }

    public String getSezione() {
        return this.sezione;
    }

    public Classe sezione(String sezione) {
        this.setSezione(sezione);
        return this;
    }

    public void setSezione(String sezione) {
        this.sezione = sezione;
    }

    public String getNote() {
        return this.note;
    }

    public Classe note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Alunno> getAlunnis() {
        return this.alunnis;
    }

    public void setAlunnis(Set<Alunno> alunnos) {
        if (this.alunnis != null) {
            this.alunnis.forEach(i -> i.setClasseDiAppartenenza(null));
        }
        if (alunnos != null) {
            alunnos.forEach(i -> i.setClasseDiAppartenenza(this));
        }
        this.alunnis = alunnos;
    }

    public Classe alunnis(Set<Alunno> alunnos) {
        this.setAlunnis(alunnos);
        return this;
    }

    public Classe addAlunni(Alunno alunno) {
        this.alunnis.add(alunno);
        alunno.setClasseDiAppartenenza(this);
        return this;
    }

    public Classe removeAlunni(Alunno alunno) {
        this.alunnis.remove(alunno);
        alunno.setClasseDiAppartenenza(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classe)) {
            return false;
        }
        return getId() != null && getId().equals(((Classe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classe{" +
            "id=" + getId() +
            ", indicazioneNumerica=" + getIndicazioneNumerica() +
            ", sezione='" + getSezione() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
