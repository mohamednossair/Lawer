package com.lawyer.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lawyer.domain.CourtCaseType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourtCaseTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String caseTypeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourtCaseTypeDTO)) {
            return false;
        }

        CourtCaseTypeDTO courtCaseTypeDTO = (CourtCaseTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courtCaseTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourtCaseTypeDTO{" +
            "id=" + getId() +
            ", caseTypeName='" + getCaseTypeName() + "'" +
            "}";
    }
}
