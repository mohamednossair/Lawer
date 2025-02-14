import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './court-case.reducer';

export const CourtCaseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const courtCaseEntity = useAppSelector(state => state.courtCase.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="courtCaseDetailsHeading">
          <Translate contentKey="lawyerApp.courtCase.detail.title">CourtCase</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.id}</dd>
          <dt>
            <span id="caseNumber">
              <Translate contentKey="lawyerApp.courtCase.caseNumber">Case Number</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.caseNumber}</dd>
          <dt>
            <span id="caseYear">
              <Translate contentKey="lawyerApp.courtCase.caseYear">Case Year</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.caseYear}</dd>
          <dt>
            <span id="courtCircuit">
              <Translate contentKey="lawyerApp.courtCase.courtCircuit">Court Circuit</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.courtCircuit}</dd>
          <dt>
            <span id="registrationDate">
              <Translate contentKey="lawyerApp.courtCase.registrationDate">Registration Date</Translate>
            </span>
          </dt>
          <dd>
            {courtCaseEntity.registrationDate ? (
              <TextFormat value={courtCaseEntity.registrationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="attorneyNumber">
              <Translate contentKey="lawyerApp.courtCase.attorneyNumber">Attorney Number</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.attorneyNumber}</dd>
          <dt>
            <span id="attorneyYear">
              <Translate contentKey="lawyerApp.courtCase.attorneyYear">Attorney Year</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.attorneyYear}</dd>
          <dt>
            <span id="attorneyAuthentication">
              <Translate contentKey="lawyerApp.courtCase.attorneyAuthentication">Attorney Authentication</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.attorneyAuthentication}</dd>
          <dt>
            <span id="opponentName">
              <Translate contentKey="lawyerApp.courtCase.opponentName">Opponent Name</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.opponentName}</dd>
          <dt>
            <span id="opponentDescription">
              <Translate contentKey="lawyerApp.courtCase.opponentDescription">Opponent Description</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.opponentDescription}</dd>
          <dt>
            <span id="opponentAddress">
              <Translate contentKey="lawyerApp.courtCase.opponentAddress">Opponent Address</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.opponentAddress}</dd>
          <dt>
            <span id="subject">
              <Translate contentKey="lawyerApp.courtCase.subject">Subject</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.subject}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="lawyerApp.courtCase.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{courtCaseEntity.notes}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="lawyerApp.courtCase.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {courtCaseEntity.createdAt ? <TextFormat value={courtCaseEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="lawyerApp.courtCase.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {courtCaseEntity.updatedAt ? <TextFormat value={courtCaseEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="lawyerApp.courtCase.court">Court</Translate>
          </dt>
          <dd>{courtCaseEntity.court ? courtCaseEntity.court.courtName : ''}</dd>
          <dt>
            <Translate contentKey="lawyerApp.courtCase.client">Client</Translate>
          </dt>
          <dd>{courtCaseEntity.client ? courtCaseEntity.client.clientName : ''}</dd>
          <dt>
            <Translate contentKey="lawyerApp.courtCase.courtCaseType">Court Case Type</Translate>
          </dt>
          <dd>{courtCaseEntity.courtCaseType ? courtCaseEntity.courtCaseType.caseTypeName : ''}</dd>
          <dt>
            <Translate contentKey="lawyerApp.courtCase.caseStatus">Case Status</Translate>
          </dt>
          <dd>{courtCaseEntity.caseStatus ? courtCaseEntity.caseStatus.caseStatusName : ''}</dd>
          <dt>
            <Translate contentKey="lawyerApp.courtCase.opponentLawyerId">Opponent Lawyer Id</Translate>
          </dt>
          <dd>{courtCaseEntity.opponentLawyerId ? courtCaseEntity.opponentLawyerId.lawyerName : ''}</dd>
        </dl>
        <Button tag={Link} to="/court-case" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/court-case/${courtCaseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CourtCaseDetail;
