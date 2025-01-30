import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './case-document.reducer';

export const CaseDocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const caseDocumentEntity = useAppSelector(state => state.caseDocument.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="caseDocumentDetailsHeading">
          <Translate contentKey="lawyerApp.caseDocument.detail.title">CaseDocument</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{caseDocumentEntity.id}</dd>
          <dt>
            <span id="documentName">
              <Translate contentKey="lawyerApp.caseDocument.documentName">Document Name</Translate>
            </span>
          </dt>
          <dd>{caseDocumentEntity.documentName}</dd>
          <dt>
            <span id="documentType">
              <Translate contentKey="lawyerApp.caseDocument.documentType">Document Type</Translate>
            </span>
          </dt>
          <dd>{caseDocumentEntity.documentType}</dd>
          <dt>
            <span id="filePath">
              <Translate contentKey="lawyerApp.caseDocument.filePath">File Path</Translate>
            </span>
          </dt>
          <dd>{caseDocumentEntity.filePath}</dd>
          <dt>
            <span id="uploadedBy">
              <Translate contentKey="lawyerApp.caseDocument.uploadedBy">Uploaded By</Translate>
            </span>
          </dt>
          <dd>{caseDocumentEntity.uploadedBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="lawyerApp.caseDocument.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {caseDocumentEntity.createdAt ? <TextFormat value={caseDocumentEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="lawyerApp.caseDocument.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {caseDocumentEntity.updatedAt ? <TextFormat value={caseDocumentEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="lawyerApp.caseDocument.courtCase">Court Case</Translate>
          </dt>
          <dd>{caseDocumentEntity.courtCase ? caseDocumentEntity.courtCase.id : ''}</dd>
          <dt>
            <Translate contentKey="lawyerApp.caseDocument.user">User</Translate>
          </dt>
          <dd>{caseDocumentEntity.user ? caseDocumentEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/case-document" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/case-document/${caseDocumentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CaseDocumentDetail;
