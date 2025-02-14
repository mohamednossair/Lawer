package com.lawyer.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.lawyer.domain.CaseSession} entity. This class is used
 * in {@link com.lawyer.web.rest.CaseSessionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /case-sessions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaseSessionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter sessionDate;

    private ZonedDateTimeFilter sessionTime;

    private StringFilter description;

    private StringFilter notes;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter courtCaseId;

    private Boolean distinct;

    public CaseSessionCriteria() {}

    public CaseSessionCriteria(CaseSessionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sessionDate = other.optionalSessionDate().map(LocalDateFilter::copy).orElse(null);
        this.sessionTime = other.optionalSessionTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.courtCaseId = other.optionalCourtCaseId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CaseSessionCriteria copy() {
        return new CaseSessionCriteria(this);
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

    public LocalDateFilter getSessionDate() {
        return sessionDate;
    }

    public Optional<LocalDateFilter> optionalSessionDate() {
        return Optional.ofNullable(sessionDate);
    }

    public LocalDateFilter sessionDate() {
        if (sessionDate == null) {
            setSessionDate(new LocalDateFilter());
        }
        return sessionDate;
    }

    public void setSessionDate(LocalDateFilter sessionDate) {
        this.sessionDate = sessionDate;
    }

    public ZonedDateTimeFilter getSessionTime() {
        return sessionTime;
    }

    public Optional<ZonedDateTimeFilter> optionalSessionTime() {
        return Optional.ofNullable(sessionTime);
    }

    public ZonedDateTimeFilter sessionTime() {
        if (sessionTime == null) {
            setSessionTime(new ZonedDateTimeFilter());
        }
        return sessionTime;
    }

    public void setSessionTime(ZonedDateTimeFilter sessionTime) {
        this.sessionTime = sessionTime;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public LongFilter getCourtCaseId() {
        return courtCaseId;
    }

    public Optional<LongFilter> optionalCourtCaseId() {
        return Optional.ofNullable(courtCaseId);
    }

    public LongFilter courtCaseId() {
        if (courtCaseId == null) {
            setCourtCaseId(new LongFilter());
        }
        return courtCaseId;
    }

    public void setCourtCaseId(LongFilter courtCaseId) {
        this.courtCaseId = courtCaseId;
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
        final CaseSessionCriteria that = (CaseSessionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sessionDate, that.sessionDate) &&
            Objects.equals(sessionTime, that.sessionTime) &&
            Objects.equals(description, that.description) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(courtCaseId, that.courtCaseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionDate, sessionTime, description, notes, createdAt, updatedAt, courtCaseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseSessionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSessionDate().map(f -> "sessionDate=" + f + ", ").orElse("") +
            optionalSessionTime().map(f -> "sessionTime=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalCourtCaseId().map(f -> "courtCaseId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
