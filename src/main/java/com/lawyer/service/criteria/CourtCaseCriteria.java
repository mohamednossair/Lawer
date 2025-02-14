package com.lawyer.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.lawyer.domain.CourtCase} entity. This class is used
 * in {@link com.lawyer.web.rest.CourtCaseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /court-cases?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourtCaseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter caseNumber;

    private StringFilter caseYear;

    private StringFilter courtCircuit;

    private LocalDateFilter registrationDate;

    private StringFilter attorneyNumber;

    private IntegerFilter attorneyYear;

    private StringFilter attorneyAuthentication;

    private StringFilter opponentName;

    private StringFilter opponentDescription;

    private StringFilter opponentAddress;

    private StringFilter subject;

    private StringFilter notes;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter courtId;

    private LongFilter clientId;

    private LongFilter courtCaseTypeId;

    private LongFilter caseStatusId;

    private LongFilter opponentLawyerIdId;

    private Boolean distinct;

    public CourtCaseCriteria() {}

    public CourtCaseCriteria(CourtCaseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.caseNumber = other.optionalCaseNumber().map(StringFilter::copy).orElse(null);
        this.caseYear = other.optionalCaseYear().map(StringFilter::copy).orElse(null);
        this.courtCircuit = other.optionalCourtCircuit().map(StringFilter::copy).orElse(null);
        this.registrationDate = other.optionalRegistrationDate().map(LocalDateFilter::copy).orElse(null);
        this.attorneyNumber = other.optionalAttorneyNumber().map(StringFilter::copy).orElse(null);
        this.attorneyYear = other.optionalAttorneyYear().map(IntegerFilter::copy).orElse(null);
        this.attorneyAuthentication = other.optionalAttorneyAuthentication().map(StringFilter::copy).orElse(null);
        this.opponentName = other.optionalOpponentName().map(StringFilter::copy).orElse(null);
        this.opponentDescription = other.optionalOpponentDescription().map(StringFilter::copy).orElse(null);
        this.opponentAddress = other.optionalOpponentAddress().map(StringFilter::copy).orElse(null);
        this.subject = other.optionalSubject().map(StringFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.courtId = other.optionalCourtId().map(LongFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.courtCaseTypeId = other.optionalCourtCaseTypeId().map(LongFilter::copy).orElse(null);
        this.caseStatusId = other.optionalCaseStatusId().map(LongFilter::copy).orElse(null);
        this.opponentLawyerIdId = other.optionalOpponentLawyerIdId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CourtCaseCriteria copy() {
        return new CourtCaseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCaseNumber() {
        return caseNumber;
    }

    public Optional<StringFilter> optionalCaseNumber() {
        return Optional.ofNullable(caseNumber);
    }

    public StringFilter caseNumber() {
        if (caseNumber == null) {
            setCaseNumber(new StringFilter());
        }
        return caseNumber;
    }

    public void setCaseNumber(StringFilter caseNumber) {
        this.caseNumber = caseNumber;
    }

    public StringFilter getCaseYear() {
        return caseYear;
    }

    public Optional<StringFilter> optionalCaseYear() {
        return Optional.ofNullable(caseYear);
    }

    public StringFilter caseYear() {
        if (caseYear == null) {
            setCaseYear(new StringFilter());
        }
        return caseYear;
    }

    public void setCaseYear(StringFilter caseYear) {
        this.caseYear = caseYear;
    }

    public StringFilter getCourtCircuit() {
        return courtCircuit;
    }

    public Optional<StringFilter> optionalCourtCircuit() {
        return Optional.ofNullable(courtCircuit);
    }

    public StringFilter courtCircuit() {
        if (courtCircuit == null) {
            setCourtCircuit(new StringFilter());
        }
        return courtCircuit;
    }

    public void setCourtCircuit(StringFilter courtCircuit) {
        this.courtCircuit = courtCircuit;
    }

    public LocalDateFilter getRegistrationDate() {
        return registrationDate;
    }

    public Optional<LocalDateFilter> optionalRegistrationDate() {
        return Optional.ofNullable(registrationDate);
    }

    public LocalDateFilter registrationDate() {
        if (registrationDate == null) {
            setRegistrationDate(new LocalDateFilter());
        }
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public StringFilter getAttorneyNumber() {
        return attorneyNumber;
    }

    public Optional<StringFilter> optionalAttorneyNumber() {
        return Optional.ofNullable(attorneyNumber);
    }

    public StringFilter attorneyNumber() {
        if (attorneyNumber == null) {
            setAttorneyNumber(new StringFilter());
        }
        return attorneyNumber;
    }

    public void setAttorneyNumber(StringFilter attorneyNumber) {
        this.attorneyNumber = attorneyNumber;
    }

    public IntegerFilter getAttorneyYear() {
        return attorneyYear;
    }

    public Optional<IntegerFilter> optionalAttorneyYear() {
        return Optional.ofNullable(attorneyYear);
    }

    public IntegerFilter attorneyYear() {
        if (attorneyYear == null) {
            setAttorneyYear(new IntegerFilter());
        }
        return attorneyYear;
    }

    public void setAttorneyYear(IntegerFilter attorneyYear) {
        this.attorneyYear = attorneyYear;
    }

    public StringFilter getAttorneyAuthentication() {
        return attorneyAuthentication;
    }

    public Optional<StringFilter> optionalAttorneyAuthentication() {
        return Optional.ofNullable(attorneyAuthentication);
    }

    public StringFilter attorneyAuthentication() {
        if (attorneyAuthentication == null) {
            setAttorneyAuthentication(new StringFilter());
        }
        return attorneyAuthentication;
    }

    public void setAttorneyAuthentication(StringFilter attorneyAuthentication) {
        this.attorneyAuthentication = attorneyAuthentication;
    }

    public StringFilter getOpponentName() {
        return opponentName;
    }

    public Optional<StringFilter> optionalOpponentName() {
        return Optional.ofNullable(opponentName);
    }

    public StringFilter opponentName() {
        if (opponentName == null) {
            setOpponentName(new StringFilter());
        }
        return opponentName;
    }

    public void setOpponentName(StringFilter opponentName) {
        this.opponentName = opponentName;
    }

    public StringFilter getOpponentDescription() {
        return opponentDescription;
    }

    public Optional<StringFilter> optionalOpponentDescription() {
        return Optional.ofNullable(opponentDescription);
    }

    public StringFilter opponentDescription() {
        if (opponentDescription == null) {
            setOpponentDescription(new StringFilter());
        }
        return opponentDescription;
    }

    public void setOpponentDescription(StringFilter opponentDescription) {
        this.opponentDescription = opponentDescription;
    }

    public StringFilter getOpponentAddress() {
        return opponentAddress;
    }

    public Optional<StringFilter> optionalOpponentAddress() {
        return Optional.ofNullable(opponentAddress);
    }

    public StringFilter opponentAddress() {
        if (opponentAddress == null) {
            setOpponentAddress(new StringFilter());
        }
        return opponentAddress;
    }

    public void setOpponentAddress(StringFilter opponentAddress) {
        this.opponentAddress = opponentAddress;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public Optional<StringFilter> optionalSubject() {
        return Optional.ofNullable(subject);
    }

    public StringFilter subject() {
        if (subject == null) {
            setSubject(new StringFilter());
        }
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getCourtId() {
        return courtId;
    }

    public Optional<LongFilter> optionalCourtId() {
        return Optional.ofNullable(courtId);
    }

    public LongFilter courtId() {
        if (courtId == null) {
            setCourtId(new LongFilter());
        }
        return courtId;
    }

    public void setCourtId(LongFilter courtId) {
        this.courtId = courtId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public Optional<LongFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public LongFilter clientId() {
        if (clientId == null) {
            setClientId(new LongFilter());
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getCourtCaseTypeId() {
        return courtCaseTypeId;
    }

    public Optional<LongFilter> optionalCourtCaseTypeId() {
        return Optional.ofNullable(courtCaseTypeId);
    }

    public LongFilter courtCaseTypeId() {
        if (courtCaseTypeId == null) {
            setCourtCaseTypeId(new LongFilter());
        }
        return courtCaseTypeId;
    }

    public void setCourtCaseTypeId(LongFilter courtCaseTypeId) {
        this.courtCaseTypeId = courtCaseTypeId;
    }

    public LongFilter getCaseStatusId() {
        return caseStatusId;
    }

    public Optional<LongFilter> optionalCaseStatusId() {
        return Optional.ofNullable(caseStatusId);
    }

    public LongFilter caseStatusId() {
        if (caseStatusId == null) {
            setCaseStatusId(new LongFilter());
        }
        return caseStatusId;
    }

    public void setCaseStatusId(LongFilter caseStatusId) {
        this.caseStatusId = caseStatusId;
    }

    public LongFilter getOpponentLawyerIdId() {
        return opponentLawyerIdId;
    }

    public Optional<LongFilter> optionalOpponentLawyerIdId() {
        return Optional.ofNullable(opponentLawyerIdId);
    }

    public LongFilter opponentLawyerIdId() {
        if (opponentLawyerIdId == null) {
            setOpponentLawyerIdId(new LongFilter());
        }
        return opponentLawyerIdId;
    }

    public void setOpponentLawyerIdId(LongFilter opponentLawyerIdId) {
        this.opponentLawyerIdId = opponentLawyerIdId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourtCaseCriteria that = (CourtCaseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(caseNumber, that.caseNumber) &&
            Objects.equals(caseYear, that.caseYear) &&
            Objects.equals(courtCircuit, that.courtCircuit) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(attorneyNumber, that.attorneyNumber) &&
            Objects.equals(attorneyYear, that.attorneyYear) &&
            Objects.equals(attorneyAuthentication, that.attorneyAuthentication) &&
            Objects.equals(opponentName, that.opponentName) &&
            Objects.equals(opponentDescription, that.opponentDescription) &&
            Objects.equals(opponentAddress, that.opponentAddress) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(courtId, that.courtId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(courtCaseTypeId, that.courtCaseTypeId) &&
            Objects.equals(caseStatusId, that.caseStatusId) &&
            Objects.equals(opponentLawyerIdId, that.opponentLawyerIdId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            caseNumber,
            caseYear,
            courtCircuit,
            registrationDate,
            attorneyNumber,
            attorneyYear,
            attorneyAuthentication,
            opponentName,
            opponentDescription,
            opponentAddress,
            subject,
            notes,
            createdAt,
            updatedAt,
            courtId,
            clientId,
            courtCaseTypeId,
            caseStatusId,
            opponentLawyerIdId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourtCaseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCaseNumber().map(f -> "caseNumber=" + f + ", ").orElse("") +
            optionalCaseYear().map(f -> "caseYear=" + f + ", ").orElse("") +
            optionalCourtCircuit().map(f -> "courtCircuit=" + f + ", ").orElse("") +
            optionalRegistrationDate().map(f -> "registrationDate=" + f + ", ").orElse("") +
            optionalAttorneyNumber().map(f -> "attorneyNumber=" + f + ", ").orElse("") +
            optionalAttorneyYear().map(f -> "attorneyYear=" + f + ", ").orElse("") +
            optionalAttorneyAuthentication().map(f -> "attorneyAuthentication=" + f + ", ").orElse("") +
            optionalOpponentName().map(f -> "opponentName=" + f + ", ").orElse("") +
            optionalOpponentDescription().map(f -> "opponentDescription=" + f + ", ").orElse("") +
            optionalOpponentAddress().map(f -> "opponentAddress=" + f + ", ").orElse("") +
            optionalSubject().map(f -> "subject=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalCourtId().map(f -> "courtId=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalCourtCaseTypeId().map(f -> "courtCaseTypeId=" + f + ", ").orElse("") +
            optionalCaseStatusId().map(f -> "caseStatusId=" + f + ", ").orElse("") +
            optionalOpponentLawyerIdId().map(f -> "opponentLawyerIdId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
