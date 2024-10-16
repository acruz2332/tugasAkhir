package com.proyek.akhir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.proyek.akhir.domain.History} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String predictionInput;

    @NotNull
    private String predictionResult;

    private UserDTO user;

    @NotNull
    private Instant createdDate;


    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPredictionInput() {
        return predictionInput;
    }

    public void setPredictionInput(String predictionInput) {
        this.predictionInput = predictionInput;
    }

    public String getPredictionResult() {
        return predictionResult;
    }

    public void setPredictionResult(String predictionResult) {
        this.predictionResult = predictionResult;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoryDTO)) {
            return false;
        }

        HistoryDTO historyDTO = (HistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryDTO{" +
            "id=" + getId() +
            ", predictionInput='" + getPredictionInput() + "'" +
            ", predictionResult='" + getPredictionResult() + "'" +
            ", user=" + getUser() +
            ", createdDate=" + getCreatedDate() +
            "}";
    }


}
