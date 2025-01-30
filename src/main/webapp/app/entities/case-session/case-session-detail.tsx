import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './case-session.reducer';

export const CaseSessionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const caseSessionEntity = useAppSelector(state => state.caseSession.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="caseSessionDetailsHeading">
          <Translate contentKey="lawyerApp.caseSession.detail.title">CaseSession</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{caseSessionEntity.id}</dd>
          <dt>
            <span id="sessionDate">
              <Translate contentKey="lawyerApp.caseSession.sessionDate">Session Date</Translate>
            </span>
          </dt>
          <dd>
            {caseSessionEntity.sessionDate ? (
              <TextFormat value={caseSessionEntity.sessionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="sessionTime">
              <Translate contentKey="lawyerApp.caseSession.sessionTime">Session Time</Translate>
            </span>
          </dt>
          <dd>
            {caseSessionEntity.sessionTime ? (
              <TextFormat value={caseSessionEntity.sessionTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="lawyerApp.caseSession.description">Description</Translate>
            </span>
          </dt>
          <dd>{caseSessionEntity.description}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="lawyerApp.caseSession.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{caseSessionEntity.notes}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="lawyerApp.caseSession.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {caseSessionEntity.createdAt ? <TextFormat value={caseSessionEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="lawyerApp.caseSession.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {caseSessionEntity.updatedAt ? <TextFormat value={caseSessionEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="lawyerApp.caseSession.courtCase">Court Case</Translate>
          </dt>
          <dd>{caseSessionEntity.courtCase ? caseSessionEntity.courtCase.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/case-session" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/case-session/${caseSessionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CaseSessionDetail;
