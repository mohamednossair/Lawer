package com.lawyer.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Lawyer.
 */
@Entity
@Table(name = "lawyer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Lawyer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "lawyer_name", nullable = false, unique = true)
    private String lawyerName;

    @Size(max = 2000)
    @Column(name = "address", length = 2000)
    private String address;

    @Size(max = 200)
    @Column(name = "contact_number", length = 200)
    private String contactNumber;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "email")
    private String email;

    @Size(max = 100)
    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lawyer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLawyerName() {
        return this.lawyerName;
    }

    public Lawyer lawyerName(String lawyerName) {
        this.setLawyerName(lawyerName);
        return this;
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
    }

    public String getAddress() {
        return this.address;
    }

    public Lawyer address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public Lawyer contactNumber(String contactNumber) {
        this.setContactNumber(contactNumber);
        return this;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    public Lawyer specialization(String specialization) {
        this.setSpecialization(specialization);
        return this;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return this.email;
    }

    public Lawyer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public Lawyer registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Lawyer createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Lawyer updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lawyer)) {
            return false;
        }
        return getId() != null && getId().equals(((Lawyer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lawyer{" +
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
