package com.lawyer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CaseDocument.
 */
@Entity
@Table(name = "case_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaseDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_name", nullable = false)
    private String documentName;

    @NotNull
    @Size(max = 4000)
    @Column(name = "document_type", length = 4000, nullable = false)
    private String documentType;

    @Lob
    @Column(name = "document_file", nullable = false)
    private byte[] documentFile;

    @NotNull
    @Column(name = "document_file_content_type", nullable = false)
    private String documentFileContentType;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    private Client client;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "court", "client", "courtCaseType", "caseStatus", "opponentLawyerId" }, allowSetters = true)
    private CourtCase courtCase;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CaseDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public CaseDocument documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public CaseDocument documentType(String documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public byte[] getDocumentFile() {
        return this.documentFile;
    }

    public CaseDocument documentFile(byte[] documentFile) {
        this.setDocumentFile(documentFile);
        return this;
    }

    public void setDocumentFile(byte[] documentFile) {
        this.documentFile = documentFile;
    }

    public String getDocumentFileContentType() {
        return this.documentFileContentType;
    }

    public CaseDocument documentFileContentType(String documentFileContentType) {
        this.documentFileContentType = documentFileContentType;
        return this;
    }

    public void setDocumentFileContentType(String documentFileContentType) {
        this.documentFileContentType = documentFileContentType;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public CaseDocument createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public CaseDocument updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CaseDocument client(Client client) {
        this.setClient(client);
        return this;
    }

    public CourtCase getCourtCase() {
        return this.courtCase;
    }

    public void setCourtCase(CourtCase courtCase) {
        this.courtCase = courtCase;
    }

    public CaseDocument courtCase(CourtCase courtCase) {
        this.setCourtCase(courtCase);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CaseDocument user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaseDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((CaseDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseDocument{" +
            "id=" + getId() +
            ", documentName='" + getDocumentName() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", documentFile='" + getDocumentFile() + "'" +
            ", documentFileContentType='" + getDocumentFileContentType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
