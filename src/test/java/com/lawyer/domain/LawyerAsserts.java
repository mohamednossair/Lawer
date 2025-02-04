package com.lawyer.domain;

import static com.lawyer.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class LawyerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLawyerAllPropertiesEquals(Lawyer expected, Lawyer actual) {
        assertLawyerAutoGeneratedPropertiesEquals(expected, actual);
        assertLawyerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLawyerAllUpdatablePropertiesEquals(Lawyer expected, Lawyer actual) {
        assertLawyerUpdatableFieldsEquals(expected, actual);
        assertLawyerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLawyerAutoGeneratedPropertiesEquals(Lawyer expected, Lawyer actual) {
        assertThat(expected)
            .as("Verify Lawyer auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLawyerUpdatableFieldsEquals(Lawyer expected, Lawyer actual) {
        assertThat(expected)
            .as("Verify Lawyer relevant properties")
            .satisfies(e -> assertThat(e.getLawyerName()).as("check lawyerName").isEqualTo(actual.getLawyerName()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getContactNumber()).as("check contactNumber").isEqualTo(actual.getContactNumber()))
            .satisfies(e -> assertThat(e.getSpecialization()).as("check specialization").isEqualTo(actual.getSpecialization()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getRegistrationNumber()).as("check registrationNumber").isEqualTo(actual.getRegistrationNumber()))
            .satisfies(e ->
                assertThat(e.getCreatedAt())
                    .as("check createdAt")
                    .usingComparator(zonedDataTimeSameInstant)
                    .isEqualTo(actual.getCreatedAt())
            )
            .satisfies(e ->
                assertThat(e.getUpdatedAt())
                    .as("check updatedAt")
                    .usingComparator(zonedDataTimeSameInstant)
                    .isEqualTo(actual.getUpdatedAt())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLawyerUpdatableRelationshipsEquals(Lawyer expected, Lawyer actual) {
        // empty method
    }
}
