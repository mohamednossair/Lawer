package com.lawyer.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.lawyer.domain.CaseDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaseDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String documentName;

    @NotNull
    @Size(max = 4000)
    private String documentType;

    @Lob
    private byte[] documentFile;

    private String documentFileContentType;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private CourtCaseDTO courtCase;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public byte[] getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(byte[] documentFile) {
        this.documentFile = documentFile;
    }

    public String getDocumentFileContentType() {
        return documentFileContentType;
    }

    public void setDocumentFileContentType(String documentFileContentType) {
        this.documentFileContentType = documentFileContentType;
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

    public CourtCaseDTO getCourtCase() {
        return courtCase;
    }

    public void setCourtCase(CourtCaseDTO courtCase) {
        this.courtCase = courtCase;
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
        if (!(o instanceof CaseDocumentDTO)) {
            return false;
        }

        CaseDocumentDTO caseDocumentDTO = (CaseDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, caseDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseDocumentDTO{" +
            "id=" + getId() +
            ", documentName='" + getDocumentName() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", documentFile='" + getDocumentFile() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", courtCase=" + getCourtCase() +
            ", user=" + getUser() +
            "}";
    }
}
