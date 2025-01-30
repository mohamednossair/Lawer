import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './client.reducer';

export const ClientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const clientEntity = useAppSelector(state => state.client.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientDetailsHeading">
          <Translate contentKey="lawyerApp.client.detail.title">Client</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clientEntity.id}</dd>
          <dt>
            <span id="clientName">
              <Translate contentKey="lawyerApp.client.clientName">Client Name</Translate>
            </span>
          </dt>
          <dd>{clientEntity.clientName}</dd>
          <dt>
            <span id="clientDescription">
              <Translate contentKey="lawyerApp.client.clientDescription">Client Description</Translate>
            </span>
          </dt>
          <dd>{clientEntity.clientDescription}</dd>
          <dt>
            <span id="contactNumber">
              <Translate contentKey="lawyerApp.client.contactNumber">Contact Number</Translate>
            </span>
          </dt>
          <dd>{clientEntity.contactNumber}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="lawyerApp.client.address">Address</Translate>
            </span>
          </dt>
          <dd>{clientEntity.address}</dd>
          <dt>
            <span id="nationalId">
              <Translate contentKey="lawyerApp.client.nationalId">National Id</Translate>
            </span>
          </dt>
          <dd>{clientEntity.nationalId}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="lawyerApp.client.email">Email</Translate>
            </span>
          </dt>
          <dd>{clientEntity.email}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="lawyerApp.client.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{clientEntity.createdAt ? <TextFormat value={clientEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="lawyerApp.client.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{clientEntity.updatedAt ? <TextFormat value={clientEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/client" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client/${clientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClientDetail;
