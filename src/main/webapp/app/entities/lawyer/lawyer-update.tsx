import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './lawyer.reducer';

export const LawyerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const lawyerEntity = useAppSelector(state => state.lawyer.entity);
  const loading = useAppSelector(state => state.lawyer.loading);
  const updating = useAppSelector(state => state.lawyer.updating);
  const updateSuccess = useAppSelector(state => state.lawyer.updateSuccess);

  const handleClose = () => {
    navigate(`/lawyer${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...lawyerEntity,
      ...values,
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
          ...lawyerEntity,
          createdAt: convertDateTimeFromServer(lawyerEntity.createdAt),
          updatedAt: convertDateTimeFromServer(lawyerEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lawyerApp.lawyer.home.createOrEditLabel" data-cy="LawyerCreateUpdateHeading">
            <Translate contentKey="lawyerApp.lawyer.home.createOrEditLabel">Create or edit a Lawyer</Translate>
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
                  id="lawyer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('lawyerApp.lawyer.lawyerName')}
                id="lawyer-lawyerName"
                name="lawyerName"
                data-cy="lawyerName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.lawyer.address')}
                id="lawyer-address"
                name="address"
                data-cy="address"
                type="text"
                validate={{
                  maxLength: { value: 2000, message: translate('entity.validation.maxlength', { max: 2000 }) },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.lawyer.contactNumber')}
                id="lawyer-contactNumber"
                name="contactNumber"
                data-cy="contactNumber"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.lawyer.specialization')}
                id="lawyer-specialization"
                name="specialization"
                data-cy="specialization"
                type="text"
              />
              <ValidatedField label={translate('lawyerApp.lawyer.email')} id="lawyer-email" name="email" data-cy="email" type="text" />
              <ValidatedField
                label={translate('lawyerApp.lawyer.registrationNumber')}
                id="lawyer-registrationNumber"
                name="registrationNumber"
                data-cy="registrationNumber"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('lawyerApp.lawyer.createdAt')}
                id="lawyer-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('lawyerApp.lawyer.updatedAt')}
                id="lawyer-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/lawyer" replace color="info">
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

export default LawyerUpdate;
