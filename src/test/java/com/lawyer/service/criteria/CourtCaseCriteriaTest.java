package com.lawyer.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CourtCaseCriteriaTest {

    @Test
    void newCourtCaseCriteriaHasAllFiltersNullTest() {
        var courtCaseCriteria = new CourtCaseCriteria();
        assertThat(courtCaseCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void courtCaseCriteriaFluentMethodsCreatesFiltersTest() {
        var courtCaseCriteria = new CourtCaseCriteria();

        setAllFilters(courtCaseCriteria);

        assertThat(courtCaseCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void courtCaseCriteriaCopyCreatesNullFilterTest() {
        var courtCaseCriteria = new CourtCaseCriteria();
        var copy = courtCaseCriteria.copy();

        assertThat(courtCaseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(courtCaseCriteria)
        );
    }

    @Test
    void courtCaseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var courtCaseCriteria = new CourtCaseCriteria();
        setAllFilters(courtCaseCriteria);

        var copy = courtCaseCriteria.copy();

        assertThat(courtCaseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(courtCaseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var courtCaseCriteria = new CourtCaseCriteria();

        assertThat(courtCaseCriteria).hasToString("CourtCaseCriteria{}");
    }

    private static void setAllFilters(CourtCaseCriteria courtCaseCriteria) {
        courtCaseCriteria.id();
        courtCaseCriteria.caseNumber();
        courtCaseCriteria.caseYear();
        courtCaseCriteria.courtCircuit();
        courtCaseCriteria.registrationDate();
        courtCaseCriteria.attorneyNumber();
        courtCaseCriteria.attorneyYear();
        courtCaseCriteria.attorneyAuthentication();
        courtCaseCriteria.opponentName();
        courtCaseCriteria.opponentDescription();
        courtCaseCriteria.opponentAddress();
        courtCaseCriteria.subject();
        courtCaseCriteria.notes();
        courtCaseCriteria.createdAt();
        courtCaseCriteria.updatedAt();
        courtCaseCriteria.courtId();
        courtCaseCriteria.clientId();
        courtCaseCriteria.courtCaseTypeId();
        courtCaseCriteria.caseStatusId();
        courtCaseCriteria.opponentLawyerIdId();
        courtCaseCriteria.distinct();
    }

    private static Condition<CourtCaseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCaseNumber()) &&
                condition.apply(criteria.getCaseYear()) &&
                condition.apply(criteria.getCourtCircuit()) &&
                condition.apply(criteria.getRegistrationDate()) &&
                condition.apply(criteria.getAttorneyNumber()) &&
                condition.apply(criteria.getAttorneyYear()) &&
                condition.apply(criteria.getAttorneyAuthentication()) &&
                condition.apply(criteria.getOpponentName()) &&
                condition.apply(criteria.getOpponentDescription()) &&
                condition.apply(criteria.getOpponentAddress()) &&
                condition.apply(criteria.getSubject()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getCourtId()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getCourtCaseTypeId()) &&
                condition.apply(criteria.getCaseStatusId()) &&
                condition.apply(criteria.getOpponentLawyerIdId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CourtCaseCriteria> copyFiltersAre(CourtCaseCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCaseNumber(), copy.getCaseNumber()) &&
                condition.apply(criteria.getCaseYear(), copy.getCaseYear()) &&
                condition.apply(criteria.getCourtCircuit(), copy.getCourtCircuit()) &&
                condition.apply(criteria.getRegistrationDate(), copy.getRegistrationDate()) &&
                condition.apply(criteria.getAttorneyNumber(), copy.getAttorneyNumber()) &&
                condition.apply(criteria.getAttorneyYear(), copy.getAttorneyYear()) &&
                condition.apply(criteria.getAttorneyAuthentication(), copy.getAttorneyAuthentication()) &&
                condition.apply(criteria.getOpponentName(), copy.getOpponentName()) &&
                condition.apply(criteria.getOpponentDescription(), copy.getOpponentDescription()) &&
                condition.apply(criteria.getOpponentAddress(), copy.getOpponentAddress()) &&
                condition.apply(criteria.getSubject(), copy.getSubject()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getCourtId(), copy.getCourtId()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getCourtCaseTypeId(), copy.getCourtCaseTypeId()) &&
                condition.apply(criteria.getCaseStatusId(), copy.getCaseStatusId()) &&
                condition.apply(criteria.getOpponentLawyerIdId(), copy.getOpponentLawyerIdId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
