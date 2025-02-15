package com.lawyer.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.lawyer.domain.CaseSession} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaseSessionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate sessionDate;

    private ZonedDateTime sessionTime;

    @Size(max = 4000)
    private String description;

    @Size(max = 4000)
    private String notes;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    @NotNull
    private ClientDTO client;

    @NotNull
    private CourtCaseDTO courtCase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public ZonedDateTime getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(ZonedDateTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public CourtCaseDTO getCourtCase() {
        return courtCase;
    }

    public void setCourtCase(CourtCaseDTO courtCase) {
        this.courtCase = courtCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaseSessionDTO)) {
            return false;
        }

        CaseSessionDTO caseSessionDTO = (CaseSessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, caseSessionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseSessionDTO{" +
            "id=" + getId() +
            ", sessionDate='" + getSessionDate() + "'" +
            ", sessionTime='" + getSessionTime() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", client=" + getClient() +
            ", courtCase=" + getCourtCase() +
            "}";
    }
}
