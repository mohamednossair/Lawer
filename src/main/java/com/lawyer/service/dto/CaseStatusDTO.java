package com.lawyer.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lawyer.domain.CaseStatus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaseStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String caseStatusName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseStatusName() {
        return caseStatusName;
    }

    public void setCaseStatusName(String caseStatusName) {
        this.caseStatusName = caseStatusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaseStatusDTO)) {
            return false;
        }

        CaseStatusDTO caseStatusDTO = (CaseStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, caseStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseStatusDTO{" +
            "id=" + getId() +
            ", caseStatusName='" + getCaseStatusName() + "'" +
            "}";
    }
}
