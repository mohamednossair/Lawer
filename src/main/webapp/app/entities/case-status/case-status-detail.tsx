import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './case-status.reducer';

export const CaseStatusDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const caseStatusEntity = useAppSelector(state => state.caseStatus.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="caseStatusDetailsHeading">
          <Translate contentKey="lawyerApp.caseStatus.detail.title">CaseStatus</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{caseStatusEntity.id}</dd>
          <dt>
            <span id="caseStatusName">
              <Translate contentKey="lawyerApp.caseStatus.caseStatusName">Case Status Name</Translate>
            </span>
          </dt>
          <dd>{caseStatusEntity.caseStatusName}</dd>
        </dl>
        <Button tag={Link} to="/case-status" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/case-status/${caseStatusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CaseStatusDetail;
