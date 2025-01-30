package com.lawyer.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CourtCaseTypeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCourtCaseTypeAllPropertiesEquals(CourtCaseType expected, CourtCaseType actual) {
        assertCourtCaseTypeAutoGeneratedPropertiesEquals(expected, actual);
        assertCourtCaseTypeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCourtCaseTypeAllUpdatablePropertiesEquals(CourtCaseType expected, CourtCaseType actual) {
        assertCourtCaseTypeUpdatableFieldsEquals(expected, actual);
        assertCourtCaseTypeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCourtCaseTypeAutoGeneratedPropertiesEquals(CourtCaseType expected, CourtCaseType actual) {
        assertThat(expected)
            .as("Verify CourtCaseType auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCourtCaseTypeUpdatableFieldsEquals(CourtCaseType expected, CourtCaseType actual) {
        assertThat(expected)
            .as("Verify CourtCaseType relevant properties")
            .satisfies(e -> assertThat(e.getCaseTypeName()).as("check caseTypeName").isEqualTo(actual.getCaseTypeName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCourtCaseTypeUpdatableRelationshipsEquals(CourtCaseType expected, CourtCaseType actual) {
        // empty method
    }
}
