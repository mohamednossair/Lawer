package com.lawyer.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.lawyer.domain.CaseDocument} entity. This class is used
 * in {@link com.lawyer.web.rest.CaseDocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /case-documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaseDocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentName;

    private StringFilter documentType;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter clientId;

    private LongFilter courtCaseId;

    private LongFilter userId;

    private Boolean distinct;

    public CaseDocumentCriteria() {}

    public CaseDocumentCriteria(CaseDocumentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentName = other.optionalDocumentName().map(StringFilter::copy).orElse(null);
        this.documentType = other.optionalDocumentType().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.courtCaseId = other.optionalCourtCaseId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CaseDocumentCriteria copy() {
        return new CaseDocumentCriteria(this);
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

    public StringFilter getDocumentName() {
        return documentName;
    }

    public Optional<StringFilter> optionalDocumentName() {
        return Optional.ofNullable(documentName);
    }

    public StringFilter documentName() {
        if (documentName == null) {
            setDocumentName(new StringFilter());
        }
        return documentName;
    }

    public void setDocumentName(StringFilter documentName) {
        this.documentName = documentName;
    }

    public StringFilter getDocumentType() {
        return documentType;
    }

    public Optional<StringFilter> optionalDocumentType() {
        return Optional.ofNullable(documentType);
    }

    public StringFilter documentType() {
        if (documentType == null) {
            setDocumentType(new StringFilter());
        }
        return documentType;
    }

    public void setDocumentType(StringFilter documentType) {
        this.documentType = documentType;
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

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final CaseDocumentCriteria that = (CaseDocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentName, that.documentName) &&
            Objects.equals(documentType, that.documentType) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(courtCaseId, that.courtCaseId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentName, documentType, createdAt, updatedAt, clientId, courtCaseId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseDocumentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentName().map(f -> "documentName=" + f + ", ").orElse("") +
            optionalDocumentType().map(f -> "documentType=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalCourtCaseId().map(f -> "courtCaseId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
