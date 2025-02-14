package com.lawyer.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LawyerCriteriaTest {

    @Test
    void newLawyerCriteriaHasAllFiltersNullTest() {
        var lawyerCriteria = new LawyerCriteria();
        assertThat(lawyerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void lawyerCriteriaFluentMethodsCreatesFiltersTest() {
        var lawyerCriteria = new LawyerCriteria();

        setAllFilters(lawyerCriteria);

        assertThat(lawyerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void lawyerCriteriaCopyCreatesNullFilterTest() {
        var lawyerCriteria = new LawyerCriteria();
        var copy = lawyerCriteria.copy();

        assertThat(lawyerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(lawyerCriteria)
        );
    }

    @Test
    void lawyerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var lawyerCriteria = new LawyerCriteria();
        setAllFilters(lawyerCriteria);

        var copy = lawyerCriteria.copy();

        assertThat(lawyerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(lawyerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var lawyerCriteria = new LawyerCriteria();

        assertThat(lawyerCriteria).hasToString("LawyerCriteria{}");
    }

    private static void setAllFilters(LawyerCriteria lawyerCriteria) {
        lawyerCriteria.id();
        lawyerCriteria.lawyerName();
        lawyerCriteria.address();
        lawyerCriteria.contactNumber();
        lawyerCriteria.specialization();
        lawyerCriteria.email();
        lawyerCriteria.registrationNumber();
        lawyerCriteria.createdAt();
        lawyerCriteria.updatedAt();
        lawyerCriteria.distinct();
    }

    private static Condition<LawyerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLawyerName()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getContactNumber()) &&
                condition.apply(criteria.getSpecialization()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getRegistrationNumber()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LawyerCriteria> copyFiltersAre(LawyerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLawyerName(), copy.getLawyerName()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getContactNumber(), copy.getContactNumber()) &&
                condition.apply(criteria.getSpecialization(), copy.getSpecialization()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getRegistrationNumber(), copy.getRegistrationNumber()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
