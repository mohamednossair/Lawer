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
        assertThat(actual)
            .as("Verify Lawyer auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLawyerUpdatableFieldsEquals(Lawyer expected, Lawyer actual) {
        assertThat(actual)
            .as("Verify Lawyer relevant properties")
            .satisfies(a -> assertThat(a.getLawyerName()).as("check lawyerName").isEqualTo(expected.getLawyerName()))
            .satisfies(a -> assertThat(a.getAddress()).as("check address").isEqualTo(expected.getAddress()))
            .satisfies(a -> assertThat(a.getContactNumber()).as("check contactNumber").isEqualTo(expected.getContactNumber()))
            .satisfies(a -> assertThat(a.getSpecialization()).as("check specialization").isEqualTo(expected.getSpecialization()))
            .satisfies(a -> assertThat(a.getEmail()).as("check email").isEqualTo(expected.getEmail()))
            .satisfies(a -> assertThat(a.getRegistrationNumber()).as("check registrationNumber").isEqualTo(expected.getRegistrationNumber())
            )
            .satisfies(a ->
                assertThat(a.getCreatedAt())
                    .as("check createdAt")
                    .usingComparator(zonedDataTimeSameInstant)
                    .isEqualTo(expected.getCreatedAt())
            )
            .satisfies(a ->
                assertThat(a.getUpdatedAt())
                    .as("check updatedAt")
                    .usingComparator(zonedDataTimeSameInstant)
                    .isEqualTo(expected.getUpdatedAt())
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
