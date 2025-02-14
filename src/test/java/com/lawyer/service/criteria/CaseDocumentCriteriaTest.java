package com.lawyer.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CaseDocumentCriteriaTest {

    @Test
    void newCaseDocumentCriteriaHasAllFiltersNullTest() {
        var caseDocumentCriteria = new CaseDocumentCriteria();
        assertThat(caseDocumentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void caseDocumentCriteriaFluentMethodsCreatesFiltersTest() {
        var caseDocumentCriteria = new CaseDocumentCriteria();

        setAllFilters(caseDocumentCriteria);

        assertThat(caseDocumentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void caseDocumentCriteriaCopyCreatesNullFilterTest() {
        var caseDocumentCriteria = new CaseDocumentCriteria();
        var copy = caseDocumentCriteria.copy();

        assertThat(caseDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(caseDocumentCriteria)
        );
    }

    @Test
    void caseDocumentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var caseDocumentCriteria = new CaseDocumentCriteria();
        setAllFilters(caseDocumentCriteria);

        var copy = caseDocumentCriteria.copy();

        assertThat(caseDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(caseDocumentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var caseDocumentCriteria = new CaseDocumentCriteria();

        assertThat(caseDocumentCriteria).hasToString("CaseDocumentCriteria{}");
    }

    private static void setAllFilters(CaseDocumentCriteria caseDocumentCriteria) {
        caseDocumentCriteria.id();
        caseDocumentCriteria.documentName();
        caseDocumentCriteria.documentType();
        caseDocumentCriteria.createdAt();
        caseDocumentCriteria.updatedAt();
        caseDocumentCriteria.courtCaseId();
        caseDocumentCriteria.userId();
        caseDocumentCriteria.distinct();
    }

    private static Condition<CaseDocumentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentName()) &&
                condition.apply(criteria.getDocumentType()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getCourtCaseId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CaseDocumentCriteria> copyFiltersAre(
        CaseDocumentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentName(), copy.getDocumentName()) &&
                condition.apply(criteria.getDocumentType(), copy.getDocumentType()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getCourtCaseId(), copy.getCourtCaseId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
