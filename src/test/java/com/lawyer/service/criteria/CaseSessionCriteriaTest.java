package com.lawyer.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CaseSessionCriteriaTest {

    @Test
    void newCaseSessionCriteriaHasAllFiltersNullTest() {
        var caseSessionCriteria = new CaseSessionCriteria();
        assertThat(caseSessionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void caseSessionCriteriaFluentMethodsCreatesFiltersTest() {
        var caseSessionCriteria = new CaseSessionCriteria();

        setAllFilters(caseSessionCriteria);

        assertThat(caseSessionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void caseSessionCriteriaCopyCreatesNullFilterTest() {
        var caseSessionCriteria = new CaseSessionCriteria();
        var copy = caseSessionCriteria.copy();

        assertThat(caseSessionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(caseSessionCriteria)
        );
    }

    @Test
    void caseSessionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var caseSessionCriteria = new CaseSessionCriteria();
        setAllFilters(caseSessionCriteria);

        var copy = caseSessionCriteria.copy();

        assertThat(caseSessionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(caseSessionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var caseSessionCriteria = new CaseSessionCriteria();

        assertThat(caseSessionCriteria).hasToString("CaseSessionCriteria{}");
    }

    private static void setAllFilters(CaseSessionCriteria caseSessionCriteria) {
        caseSessionCriteria.id();
        caseSessionCriteria.sessionDate();
        caseSessionCriteria.sessionTime();
        caseSessionCriteria.description();
        caseSessionCriteria.notes();
        caseSessionCriteria.createdAt();
        caseSessionCriteria.updatedAt();
        caseSessionCriteria.courtCaseId();
        caseSessionCriteria.distinct();
    }

    private static Condition<CaseSessionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSessionDate()) &&
                condition.apply(criteria.getSessionTime()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getCourtCaseId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CaseSessionCriteria> copyFiltersAre(CaseSessionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSessionDate(), copy.getSessionDate()) &&
                condition.apply(criteria.getSessionTime(), copy.getSessionTime()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getCourtCaseId(), copy.getCourtCaseId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
