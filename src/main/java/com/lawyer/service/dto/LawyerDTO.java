package com.lawyer.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.lawyer.domain.Lawyer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LawyerDTO implements Serializable {

    private Long id;

    @NotNull
    private String lawyerName;

    @Size(max = 2000)
    private String address;

    @Size(max = 200)
    private String contactNumber;

    private String specialization;

    private String email;

    @Size(max = 100)
    private String registrationNumber;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LawyerDTO)) {
            return false;
        }

        LawyerDTO lawyerDTO = (LawyerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lawyerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LawyerDTO{" +
            "id=" + getId() +
            ", lawyerName='" + getLawyerName() + "'" +
            ", address='" + getAddress() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            ", email='" + getEmail() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
