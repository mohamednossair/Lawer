package com.lawyer.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.lawyer.domain.CourtCase} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourtCaseDTO implements Serializable {

    private Long id;

    @NotNull
    private String number;

    @NotNull
    @Size(max = 10)
    private String caseYear;

    private String courtCircuit;

    @NotNull
    private LocalDate registrationDate;

    private String attorneyNumber;

    private Integer attorneyYear;

    private String attorneyAuthentication;

    private String opponentName;

    private String opponentDescription;

    private String opponentAddress;

    @NotNull
    @Size(max = 4000)
    private String subject;

    @Size(max = 4000)
    private String notes;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    @NotNull
    private CourtDTO court;

    @NotNull
    private ClientDTO client;

    @NotNull
    private CourtCaseTypeDTO courtCaseType;

    @NotNull
    private CaseStatusDTO caseStatus;

    private LawyerDTO opponentLawyerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCaseYear() {
        return caseYear;
    }

    public void setCaseYear(String caseYear) {
        this.caseYear = caseYear;
    }

    public String getCourtCircuit() {
        return courtCircuit;
    }

    public void setCourtCircuit(String courtCircuit) {
        this.courtCircuit = courtCircuit;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAttorneyNumber() {
        return attorneyNumber;
    }

    public void setAttorneyNumber(String attorneyNumber) {
        this.attorneyNumber = attorneyNumber;
    }

    public Integer getAttorneyYear() {
        return attorneyYear;
    }

    public void setAttorneyYear(Integer attorneyYear) {
        this.attorneyYear = attorneyYear;
    }

    public String getAttorneyAuthentication() {
        return attorneyAuthentication;
    }

    public void setAttorneyAuthentication(String attorneyAuthentication) {
        this.attorneyAuthentication = attorneyAuthentication;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getOpponentDescription() {
        return opponentDescription;
    }

    public void setOpponentDescription(String opponentDescription) {
        this.opponentDescription = opponentDescription;
    }

    public String getOpponentAddress() {
        return opponentAddress;
    }

    public void setOpponentAddress(String opponentAddress) {
        this.opponentAddress = opponentAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public CourtDTO getCourt() {
        return court;
    }

    public void setCourt(CourtDTO court) {
        this.court = court;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public CourtCaseTypeDTO getCourtCaseType() {
        return courtCaseType;
    }

    public void setCourtCaseType(CourtCaseTypeDTO courtCaseType) {
        this.courtCaseType = courtCaseType;
    }

    public CaseStatusDTO getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(CaseStatusDTO caseStatus) {
        this.caseStatus = caseStatus;
    }

    public LawyerDTO getOpponentLawyerId() {
        return opponentLawyerId;
    }

    public void setOpponentLawyerId(LawyerDTO opponentLawyerId) {
        this.opponentLawyerId = opponentLawyerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourtCaseDTO)) {
            return false;
        }

        CourtCaseDTO courtCaseDTO = (CourtCaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courtCaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourtCaseDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", caseYear='" + getCaseYear() + "'" +
            ", courtCircuit='" + getCourtCircuit() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", attorneyNumber='" + getAttorneyNumber() + "'" +
            ", attorneyYear=" + getAttorneyYear() +
            ", attorneyAuthentication='" + getAttorneyAuthentication() + "'" +
            ", opponentName='" + getOpponentName() + "'" +
            ", opponentDescription='" + getOpponentDescription() + "'" +
            ", opponentAddress='" + getOpponentAddress() + "'" +
            ", subject='" + getSubject() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", court=" + getCourt() +
            ", client=" + getClient() +
            ", courtCaseType=" + getCourtCaseType() +
            ", caseStatus=" + getCaseStatus() +
            ", opponentLawyerId=" + getOpponentLawyerId() +
            "}";
    }
}
