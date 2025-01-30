package com.lawyer.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lawyer.domain.Court} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourtDTO implements Serializable {

    private Long id;

    @NotNull
    private String courtName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourtDTO)) {
            return false;
        }

        CourtDTO courtDTO = (CourtDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courtDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourtDTO{" +
            "id=" + getId() +
            ", courtName='" + getCourtName() + "'" +
            "}";
    }
}
