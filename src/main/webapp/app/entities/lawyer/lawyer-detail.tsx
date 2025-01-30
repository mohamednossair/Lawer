import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lawyer.reducer';

export const LawyerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const lawyerEntity = useAppSelector(state => state.lawyer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lawyerDetailsHeading">
          <Translate contentKey="lawyerApp.lawyer.detail.title">Lawyer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{lawyerEntity.id}</dd>
          <dt>
            <span id="lawyerName">
              <Translate contentKey="lawyerApp.lawyer.lawyerName">Lawyer Name</Translate>
            </span>
          </dt>
          <dd>{lawyerEntity.lawyerName}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="lawyerApp.lawyer.address">Address</Translate>
            </span>
          </dt>
          <dd>{lawyerEntity.address}</dd>
          <dt>
            <span id="contactNumber">
              <Translate contentKey="lawyerApp.lawyer.contactNumber">Contact Number</Translate>
            </span>
          </dt>
          <dd>{lawyerEntity.contactNumber}</dd>
          <dt>
            <span id="specialization">
              <Translate contentKey="lawyerApp.lawyer.specialization">Specialization</Translate>
            </span>
          </dt>
          <dd>{lawyerEntity.specialization}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="lawyerApp.lawyer.email">Email</Translate>
            </span>
          </dt>
          <dd>{lawyerEntity.email}</dd>
          <dt>
            <span id="registrationNumber">
              <Translate contentKey="lawyerApp.lawyer.registrationNumber">Registration Number</Translate>
            </span>
          </dt>
          <dd>{lawyerEntity.registrationNumber}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="lawyerApp.lawyer.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{lawyerEntity.createdAt ? <TextFormat value={lawyerEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="lawyerApp.lawyer.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{lawyerEntity.updatedAt ? <TextFormat value={lawyerEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/lawyer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lawyer/${lawyerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LawyerDetail;
