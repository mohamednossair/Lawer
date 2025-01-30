package com.lawyer.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CourtCase.
 */
@Entity
@Table(name = "court_case")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourtCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Size(max = 10)
    @Column(name = "case_year", length = 10, nullable = false)
    private String caseYear;

    @Column(name = "court_circuit")
    private String courtCircuit;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "attorney_number")
    private String attorneyNumber;

    @Column(name = "attorney_year")
    private Integer attorneyYear;

    @Column(name = "attorney_authentication")
    private String attorneyAuthentication;

    @Column(name = "opponent_name")
    private String opponentName;

    @Column(name = "opponent_description")
    private String opponentDescription;

    @Column(name = "opponent_address")
    private String opponentAddress;

    @NotNull
    @Size(max = 4000)
    @Column(name = "subject", length = 4000, nullable = false)
    private String subject;

    @Size(max = 4000)
    @Column(name = "notes", length = 4000)
    private String notes;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    private Court court;

    @ManyToOne(optional = false)
    @NotNull
    private Client client;

    @ManyToOne(optional = false)
    @NotNull
    private CourtCaseType courtCaseType;

    @ManyToOne(optional = false)
    @NotNull
    private CaseStatus caseStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lawyer opponentLawyerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CourtCase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public CourtCase number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCaseYear() {
        return this.caseYear;
    }

    public CourtCase caseYear(String caseYear) {
        this.setCaseYear(caseYear);
        return this;
    }

    public void setCaseYear(String caseYear) {
        this.caseYear = caseYear;
    }

    public String getCourtCircuit() {
        return this.courtCircuit;
    }

    public CourtCase courtCircuit(String courtCircuit) {
        this.setCourtCircuit(courtCircuit);
        return this;
    }

    public void setCourtCircuit(String courtCircuit) {
        this.courtCircuit = courtCircuit;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public CourtCase registrationDate(LocalDate registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAttorneyNumber() {
        return this.attorneyNumber;
    }

    public CourtCase attorneyNumber(String attorneyNumber) {
        this.setAttorneyNumber(attorneyNumber);
        return this;
    }

    public void setAttorneyNumber(String attorneyNumber) {
        this.attorneyNumber = attorneyNumber;
    }

    public Integer getAttorneyYear() {
        return this.attorneyYear;
    }

    public CourtCase attorneyYear(Integer attorneyYear) {
        this.setAttorneyYear(attorneyYear);
        return this;
    }

    public void setAttorneyYear(Integer attorneyYear) {
        this.attorneyYear = attorneyYear;
    }

    public String getAttorneyAuthentication() {
        return this.attorneyAuthentication;
    }

    public CourtCase attorneyAuthentication(String attorneyAuthentication) {
        this.setAttorneyAuthentication(attorneyAuthentication);
        return this;
    }

    public void setAttorneyAuthentication(String attorneyAuthentication) {
        this.attorneyAuthentication = attorneyAuthentication;
    }

    public String getOpponentName() {
        return this.opponentName;
    }

    public CourtCase opponentName(String opponentName) {
        this.setOpponentName(opponentName);
        return this;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getOpponentDescription() {
        return this.opponentDescription;
    }

    public CourtCase opponentDescription(String opponentDescription) {
        this.setOpponentDescription(opponentDescription);
        return this;
    }

    public void setOpponentDescription(String opponentDescription) {
        this.opponentDescription = opponentDescription;
    }

    public String getOpponentAddress() {
        return this.opponentAddress;
    }

    public CourtCase opponentAddress(String opponentAddress) {
        this.setOpponentAddress(opponentAddress);
        return this;
    }

    public void setOpponentAddress(String opponentAddress) {
        this.opponentAddress = opponentAddress;
    }

    public String getSubject() {
        return this.subject;
    }

    public CourtCase subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotes() {
        return this.notes;
    }

    public CourtCase notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public CourtCase createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public CourtCase updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Court getCourt() {
        return this.court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public CourtCase court(Court court) {
        this.setCourt(court);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CourtCase client(Client client) {
        this.setClient(client);
        return this;
    }

    public CourtCaseType getCourtCaseType() {
        return this.courtCaseType;
    }

    public void setCourtCaseType(CourtCaseType courtCaseType) {
        this.courtCaseType = courtCaseType;
    }

    public CourtCase courtCaseType(CourtCaseType courtCaseType) {
        this.setCourtCaseType(courtCaseType);
        return this;
    }

    public CaseStatus getCaseStatus() {
        return this.caseStatus;
    }

    public void setCaseStatus(CaseStatus caseStatus) {
        this.caseStatus = caseStatus;
    }

    public CourtCase caseStatus(CaseStatus caseStatus) {
        this.setCaseStatus(caseStatus);
        return this;
    }

    public Lawyer getOpponentLawyerId() {
        return this.opponentLawyerId;
    }

    public void setOpponentLawyerId(Lawyer lawyer) {
        this.opponentLawyerId = lawyer;
    }

    public CourtCase opponentLawyerId(Lawyer lawyer) {
        this.setOpponentLawyerId(lawyer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourtCase)) {
            return false;
        }
        return getId() != null && getId().equals(((CourtCase) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourtCase{" +
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
            "}";
    }
}
