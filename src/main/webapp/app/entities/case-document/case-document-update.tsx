import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedBlobField, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntities as getCourtCases } from 'app/entities/court-case/court-case.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './case-document.reducer';

export const CaseDocumentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clients = useAppSelector(state => state.client.entities);
  const courtCases = useAppSelector(state => state.courtCase.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const caseDocumentEntity = useAppSelector(state => state.caseDocument.entity);
  const loading = useAppSelector(state => state.caseDocument.loading);
  const updating = useAppSelector(state => state.caseDocument.updating);
  const updateSuccess = useAppSelector(state => state.caseDocument.updateSuccess);

  const handleClose = () => {
    navigate(`/case-document${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClients({}));
    dispatch(getCourtCases({}));
    dispatch(getUsers({}));
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...caseDocumentEntity,
      ...values,
      client: clients.find(it => it.id.toString() === values.client?.toString()),
      courtCase: courtCases.find(it => it.id.toString() === values.courtCase?.toString()),
      user: users.find(it => it.id.toString() === values.user?.toString()),
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
          ...caseDocumentEntity,
          createdAt: convertDateTimeFromServer(caseDocumentEntity.createdAt),
          updatedAt: convertDateTimeFromServer(caseDocumentEntity.updatedAt),
          client: caseDocumentEntity?.client?.id,
          courtCase: caseDocumentEntity?.courtCase?.id,
          user: caseDocumentEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lawyerApp.caseDocument.home.createOrEditLabel" data-cy="CaseDocumentCreateUpdateHeading">
            <Translate contentKey="lawyerApp.caseDocument.home.createOrEditLabel">Create or edit a CaseDocument</Translate>
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
                  id="case-document-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('lawyerApp.caseDocument.documentName')}
                id="case-document-documentName"
                name="documentName"
                data-cy="documentName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.caseDocument.documentType')}
                id="case-document-documentType"
                name="documentType"
                data-cy="documentType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 4000, message: translate('entity.validation.maxlength', { max: 4000 }) },
                }}
              />
              <ValidatedBlobField
                label={translate('lawyerApp.caseDocument.documentFile')}
                id="case-document-documentFile"
                name="documentFile"
                data-cy="documentFile"
                openActionLabel={translate('entity.action.open')}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.caseDocument.createdAt')}
                id="case-document-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('lawyerApp.caseDocument.updatedAt')}
                id="case-document-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="case-document-client"
                name="client"
                data-cy="client"
                label={translate('lawyerApp.caseDocument.client')}
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
                id="case-document-courtCase"
                name="courtCase"
                data-cy="courtCase"
                label={translate('lawyerApp.caseDocument.courtCase')}
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
              <ValidatedField
                id="case-document-user"
                name="user"
                data-cy="user"
                label={translate('lawyerApp.caseDocument.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/case-document" replace color="info">
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

export default CaseDocumentUpdate;
