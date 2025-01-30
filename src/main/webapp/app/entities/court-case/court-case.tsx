import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './court-case.reducer';

export const CourtCase = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const courtCaseList = useAppSelector(state => state.courtCase.entities);
  const loading = useAppSelector(state => state.courtCase.loading);
  const totalItems = useAppSelector(state => state.courtCase.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="court-case-heading" data-cy="CourtCaseHeading">
        <Translate contentKey="lawyerApp.courtCase.home.title">Court Cases</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="lawyerApp.courtCase.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/court-case/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="lawyerApp.courtCase.home.createLabel">Create new Court Case</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {courtCaseList && courtCaseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="lawyerApp.courtCase.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('number')}>
                  <Translate contentKey="lawyerApp.courtCase.number">Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('number')} />
                </th>
                <th className="hand" onClick={sort('caseYear')}>
                  <Translate contentKey="lawyerApp.courtCase.caseYear">Case Year</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('caseYear')} />
                </th>
                <th className="hand" onClick={sort('courtCircuit')}>
                  <Translate contentKey="lawyerApp.courtCase.courtCircuit">Court Circuit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('courtCircuit')} />
                </th>
                <th className="hand" onClick={sort('registrationDate')}>
                  <Translate contentKey="lawyerApp.courtCase.registrationDate">Registration Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('registrationDate')} />
                </th>
                <th className="hand" onClick={sort('attorneyNumber')}>
                  <Translate contentKey="lawyerApp.courtCase.attorneyNumber">Attorney Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('attorneyNumber')} />
                </th>
                <th className="hand" onClick={sort('attorneyYear')}>
                  <Translate contentKey="lawyerApp.courtCase.attorneyYear">Attorney Year</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('attorneyYear')} />
                </th>
                <th className="hand" onClick={sort('attorneyAuthentication')}>
                  <Translate contentKey="lawyerApp.courtCase.attorneyAuthentication">Attorney Authentication</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('attorneyAuthentication')} />
                </th>
                <th className="hand" onClick={sort('opponentName')}>
                  <Translate contentKey="lawyerApp.courtCase.opponentName">Opponent Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('opponentName')} />
                </th>
                <th className="hand" onClick={sort('opponentDescription')}>
                  <Translate contentKey="lawyerApp.courtCase.opponentDescription">Opponent Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('opponentDescription')} />
                </th>
                <th className="hand" onClick={sort('opponentAddress')}>
                  <Translate contentKey="lawyerApp.courtCase.opponentAddress">Opponent Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('opponentAddress')} />
                </th>
                <th className="hand" onClick={sort('subject')}>
                  <Translate contentKey="lawyerApp.courtCase.subject">Subject</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subject')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="lawyerApp.courtCase.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="lawyerApp.courtCase.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="lawyerApp.courtCase.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th>
                  <Translate contentKey="lawyerApp.courtCase.court">Court</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="lawyerApp.courtCase.client">Client</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="lawyerApp.courtCase.courtCaseType">Court Case Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="lawyerApp.courtCase.caseStatus">Case Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="lawyerApp.courtCase.opponentLawyerId">Opponent Lawyer Id</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {courtCaseList.map((courtCase, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/court-case/${courtCase.id}`} color="link" size="sm">
                      {courtCase.id}
                    </Button>
                  </td>
                  <td>{courtCase.number}</td>
                  <td>{courtCase.caseYear}</td>
                  <td>{courtCase.courtCircuit}</td>
                  <td>
                    {courtCase.registrationDate ? (
                      <TextFormat type="date" value={courtCase.registrationDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{courtCase.attorneyNumber}</td>
                  <td>{courtCase.attorneyYear}</td>
                  <td>{courtCase.attorneyAuthentication}</td>
                  <td>{courtCase.opponentName}</td>
                  <td>{courtCase.opponentDescription}</td>
                  <td>{courtCase.opponentAddress}</td>
                  <td>{courtCase.subject}</td>
                  <td>{courtCase.notes}</td>
                  <td>{courtCase.createdAt ? <TextFormat type="date" value={courtCase.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{courtCase.updatedAt ? <TextFormat type="date" value={courtCase.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{courtCase.court ? <Link to={`/court/${courtCase.court.id}`}>{courtCase.court.id}</Link> : ''}</td>
                  <td>{courtCase.client ? <Link to={`/client/${courtCase.client.id}`}>{courtCase.client.id}</Link> : ''}</td>
                  <td>
                    {courtCase.courtCaseType ? (
                      <Link to={`/court-case-type/${courtCase.courtCaseType.id}`}>{courtCase.courtCaseType.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {courtCase.caseStatus ? <Link to={`/case-status/${courtCase.caseStatus.id}`}>{courtCase.caseStatus.id}</Link> : ''}
                  </td>
                  <td>
                    {courtCase.opponentLawyerId ? (
                      <Link to={`/lawyer/${courtCase.opponentLawyerId.id}`}>{courtCase.opponentLawyerId.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/court-case/${courtCase.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/court-case/${courtCase.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/court-case/${courtCase.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="lawyerApp.courtCase.home.notFound">No Court Cases found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={courtCaseList && courtCaseList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default CourtCase;
