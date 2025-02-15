import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntities as getCourtCases } from 'app/entities/court-case/court-case.reducer';
import { createEntity, getEntity, reset, updateEntity } from './case-session.reducer';

export const CaseSessionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clients = useAppSelector(state => state.client.entities);
  const courtCases = useAppSelector(state => state.courtCase.entities);
  const caseSessionEntity = useAppSelector(state => state.caseSession.entity);
  const loading = useAppSelector(state => state.caseSession.loading);
  const updating = useAppSelector(state => state.caseSession.updating);
  const updateSuccess = useAppSelector(state => state.caseSession.updateSuccess);

  const handleClose = () => {
    navigate(`/case-session${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClients({}));
    dispatch(getCourtCases({}));
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
    values.sessionTime = convertDateTimeToServer(values.sessionTime);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...caseSessionEntity,
      ...values,
      client: clients.find(it => it.id.toString() === values.client?.toString()),
      courtCase: courtCases.find(it => it.id.toString() === values.courtCase?.toString()),
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
          sessionTime: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...caseSessionEntity,
          sessionTime: convertDateTimeFromServer(caseSessionEntity.sessionTime),
          createdAt: convertDateTimeFromServer(caseSessionEntity.createdAt),
          updatedAt: convertDateTimeFromServer(caseSessionEntity.updatedAt),
          client: caseSessionEntity?.client?.id,
          courtCase: caseSessionEntity?.courtCase?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lawyerApp.caseSession.home.createOrEditLabel" data-cy="CaseSessionCreateUpdateHeading">
            <Translate contentKey="lawyerApp.caseSession.home.createOrEditLabel">Create or edit a CaseSession</Translate>
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
                  id="case-session-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('lawyerApp.caseSession.sessionDate')}
                id="case-session-sessionDate"
                name="sessionDate"
                data-cy="sessionDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.caseSession.sessionTime')}
                id="case-session-sessionTime"
                name="sessionTime"
                data-cy="sessionTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('lawyerApp.caseSession.description')}
                id="case-session-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 4000, message: translate('entity.validation.maxlength', { max: 4000 }) },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.caseSession.notes')}
                id="case-session-notes"
                name="notes"
                data-cy="notes"
                type="text"
                validate={{
                  maxLength: { value: 4000, message: translate('entity.validation.maxlength', { max: 4000 }) },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.caseSession.createdAt')}
                id="case-session-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('lawyerApp.caseSession.updatedAt')}
                id="case-session-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="case-session-client"
                name="client"
                data-cy="client"
                label={translate('lawyerApp.caseSession.client')}
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
                id="case-session-courtCase"
                name="courtCase"
                data-cy="courtCase"
                label={translate('lawyerApp.caseSession.courtCase')}
                type="select"
                required
              >
                <option value="" key="0" />
                {courtCases
                  ? courtCases.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.caseNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/case-session" replace color="info">
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

export default CaseSessionUpdate;
