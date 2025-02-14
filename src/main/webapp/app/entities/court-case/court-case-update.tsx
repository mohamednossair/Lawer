import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCourts } from 'app/entities/court/court.reducer';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntities as getCourtCaseTypes } from 'app/entities/court-case-type/court-case-type.reducer';
import { getEntities as getCaseStatuses } from 'app/entities/case-status/case-status.reducer';
import { getEntities as getLawyers } from 'app/entities/lawyer/lawyer.reducer';
import { createEntity, getEntity, reset, updateEntity } from './court-case.reducer';

export const CourtCaseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const courts = useAppSelector(state => state.court.entities);
  const clients = useAppSelector(state => state.client.entities);
  const courtCaseTypes = useAppSelector(state => state.courtCaseType.entities);
  const caseStatuses = useAppSelector(state => state.caseStatus.entities);
  const lawyers = useAppSelector(state => state.lawyer.entities);
  const courtCaseEntity = useAppSelector(state => state.courtCase.entity);
  const loading = useAppSelector(state => state.courtCase.loading);
  const updating = useAppSelector(state => state.courtCase.updating);
  const updateSuccess = useAppSelector(state => state.courtCase.updateSuccess);

  const handleClose = () => {
    navigate(`/court-case${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCourts({}));
    dispatch(getClients({}));
    dispatch(getCourtCaseTypes({}));
    dispatch(getCaseStatuses({}));
    dispatch(getLawyers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.attorneyYear !== undefined && typeof values.attorneyYear !== 'number') {
      values.attorneyYear = Number(values.attorneyYear);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...courtCaseEntity,
      ...values,
      court: courts.find(it => it.id.toString() === values.court?.toString()),
      client: clients.find(it => it.id.toString() === values.client?.toString()),
      courtCaseType: courtCaseTypes.find(it => it.id.toString() === values.courtCaseType?.toString()),
      caseStatus: caseStatuses.find(it => it.id.toString() === values.caseStatus?.toString()),
      opponentLawyerId: lawyers.find(it => it.id.toString() === values.opponentLawyerId?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...courtCaseEntity,
          createdAt: convertDateTimeFromServer(courtCaseEntity.createdAt),
          updatedAt: convertDateTimeFromServer(courtCaseEntity.updatedAt),
          court: courtCaseEntity?.court?.id,
          client: courtCaseEntity?.client?.id,
          courtCaseType: courtCaseEntity?.courtCaseType?.id,
          caseStatus: courtCaseEntity?.caseStatus?.id,
          opponentLawyerId: courtCaseEntity?.opponentLawyerId?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lawyerApp.courtCase.home.createOrEditLabel" data-cy="CourtCaseCreateUpdateHeading">
            <Translate contentKey="lawyerApp.courtCase.home.createOrEditLabel">Create or edit a CourtCase</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="court-case-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('lawyerApp.courtCase.caseNumber')}
                id="court-case-caseNumber"
                name="caseNumber"
                data-cy="caseNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.caseYear')}
                id="court-case-caseYear"
                name="caseYear"
                data-cy="caseYear"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.courtCircuit')}
                id="court-case-courtCircuit"
                name="courtCircuit"
                data-cy="courtCircuit"
                type="text"
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.registrationDate')}
                id="court-case-registrationDate"
                name="registrationDate"
                data-cy="registrationDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.attorneyNumber')}
                id="court-case-attorneyNumber"
                name="attorneyNumber"
                data-cy="attorneyNumber"
                type="text"
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.attorneyYear')}
                id="court-case-attorneyYear"
                name="attorneyYear"
                data-cy="attorneyYear"
                type="text"
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.attorneyAuthentication')}
                id="court-case-attorneyAuthentication"
                name="attorneyAuthentication"
                data-cy="attorneyAuthentication"
                type="text"
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.opponentName')}
                id="court-case-opponentName"
                name="opponentName"
                data-cy="opponentName"
                type="text"
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.opponentDescription')}
                id="court-case-opponentDescription"
                name="opponentDescription"
                data-cy="opponentDescription"
                type="text"
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.opponentAddress')}
                id="court-case-opponentAddress"
                name="opponentAddress"
                data-cy="opponentAddress"
                type="text"
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.subject')}
                id="court-case-subject"
                name="subject"
                data-cy="subject"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 4000, message: translate('entity.validation.maxlength', { max: 4000 }) },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.notes')}
                id="court-case-notes"
                name="notes"
                data-cy="notes"
                type="text"
                validate={{
                  maxLength: { value: 4000, message: translate('entity.validation.maxlength', { max: 4000 }) },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.createdAt')}
                id="court-case-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('lawyerApp.courtCase.updatedAt')}
                id="court-case-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="court-case-court"
                name="court"
                data-cy="court"
                label={translate('lawyerApp.courtCase.court')}
                type="select"
                required
              >
                <option value="" key="0" />
                {courts
                  ? courts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.courtName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="court-case-client"
                name="client"
                data-cy="client"
                label={translate('lawyerApp.courtCase.client')}
                type="select"
                required
              >
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.clientName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="court-case-courtCaseType"
                name="courtCaseType"
                data-cy="courtCaseType"
                label={translate('lawyerApp.courtCase.courtCaseType')}
                type="select"
                required
              >
                <option value="" key="0" />
                {courtCaseTypes
                  ? courtCaseTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.caseTypeName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="court-case-caseStatus"
                name="caseStatus"
                data-cy="caseStatus"
                label={translate('lawyerApp.courtCase.caseStatus')}
                type="select"
                required
              >
                <option value="" key="0" />
                {caseStatuses
                  ? caseStatuses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.caseStatusName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="court-case-opponentLawyerId"
                name="opponentLawyerId"
                data-cy="opponentLawyerId"
                label={translate('lawyerApp.courtCase.opponentLawyerId')}
                type="select"
              >
                <option value="" key="0" />
                {lawyers
                  ? lawyers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.lawyerName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/court-case" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CourtCaseUpdate;
