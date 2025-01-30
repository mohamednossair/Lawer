package com.lawyer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CaseSession.
 */
@Entity
@Table(name = "case_session")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaseSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "session_time")
    private ZonedDateTime sessionTime;

    @Size(max = 4000)
    @Column(name = "description", length = 4000)
    private String description;

    @Size(max = 4000)
    @Column(name = "notes", length = 4000)
    private String notes;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "court", "client", "courtCaseType", "caseStatus", "opponentLawyerId" }, allowSetters = true)
    private CourtCase courtCase;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CaseSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSessionDate() {
        return this.sessionDate;
    }

    public CaseSession sessionDate(LocalDate sessionDate) {
        this.setSessionDate(sessionDate);
        return this;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public ZonedDateTime getSessionTime() {
        return this.sessionTime;
    }

    public CaseSession sessionTime(ZonedDateTime sessionTime) {
        this.setSessionTime(sessionTime);
        return this;
    }

    public void setSessionTime(ZonedDateTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getDescription() {
        return this.description;
    }

    public CaseSession description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return this.notes;
    }

    public CaseSession notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public CaseSession createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public CaseSession updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CourtCase getCourtCase() {
        return this.courtCase;
    }

    public void setCourtCase(CourtCase courtCase) {
        this.courtCase = courtCase;
    }

    public CaseSession courtCase(CourtCase courtCase) {
        this.setCourtCase(courtCase);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaseSession)) {
            return false;
        }
        return getId() != null && getId().equals(((CaseSession) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseSession{" +
            "id=" + getId() +
            ", sessionDate='" + getSessionDate() + "'" +
            ", sessionTime='" + getSessionTime() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
