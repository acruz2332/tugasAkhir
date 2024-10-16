package com.proyek.akhir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A History.
 */
@Entity
@Table(name = "history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "prediction_input", nullable = false)
    private String predictionInput;

    @NotNull
    @Column(name = "prediction_result", nullable = false)
    private String predictionResult;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public History id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPredictionInput() {
        return this.predictionInput;
    }

    public History predictionInput(String predictionInput) {
        this.setPredictionInput(predictionInput);
        return this;
    }

    public void setPredictionInput(String predictionInput) {
        this.predictionInput = predictionInput;
    }

    public String getPredictionResult() {
        return this.predictionResult;
    }

    public History predictionResult(String predictionResult) {
        this.setPredictionResult(predictionResult);
        return this;
    }

    public void setPredictionResult(String predictionResult) {
        this.predictionResult = predictionResult;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public History user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof History)) {
            return false;
        }
        return getId() != null && getId().equals(((History) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "History{" +
            "id=" + getId() +
            ", predictionInput='" + getPredictionInput() + "'" +
            ", predictionResult='" + getPredictionResult() + "'" +
            "}";
    }
}
