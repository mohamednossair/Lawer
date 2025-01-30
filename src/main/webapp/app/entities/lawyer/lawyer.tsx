import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './lawyer.reducer';

export const Lawyer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const lawyerList = useAppSelector(state => state.lawyer.entities);
  const loading = useAppSelector(state => state.lawyer.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="lawyer-heading" data-cy="LawyerHeading">
        <Translate contentKey="lawyerApp.lawyer.home.title">Lawyers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="lawyerApp.lawyer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/lawyer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="lawyerApp.lawyer.home.createLabel">Create new Lawyer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {lawyerList && lawyerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="lawyerApp.lawyer.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('lawyerName')}>
                  <Translate contentKey="lawyerApp.lawyer.lawyerName">Lawyer Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lawyerName')} />
                </th>
                <th className="hand" onClick={sort('address')}>
                  <Translate contentKey="lawyerApp.lawyer.address">Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('address')} />
                </th>
                <th className="hand" onClick={sort('contactNumber')}>
                  <Translate contentKey="lawyerApp.lawyer.contactNumber">Contact Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contactNumber')} />
                </th>
                <th className="hand" onClick={sort('specialization')}>
                  <Translate contentKey="lawyerApp.lawyer.specialization">Specialization</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('specialization')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="lawyerApp.lawyer.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('registrationNumber')}>
                  <Translate contentKey="lawyerApp.lawyer.registrationNumber">Registration Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('registrationNumber')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="lawyerApp.lawyer.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="lawyerApp.lawyer.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {lawyerList.map((lawyer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/lawyer/${lawyer.id}`} color="link" size="sm">
                      {lawyer.id}
                    </Button>
                  </td>
                  <td>{lawyer.lawyerName}</td>
                  <td>{lawyer.address}</td>
                  <td>{lawyer.contactNumber}</td>
                  <td>{lawyer.specialization}</td>
                  <td>{lawyer.email}</td>
                  <td>{lawyer.registrationNumber}</td>
                  <td>{lawyer.createdAt ? <TextFormat type="date" value={lawyer.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{lawyer.updatedAt ? <TextFormat type="date" value={lawyer.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/lawyer/${lawyer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/lawyer/${lawyer.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/lawyer/${lawyer.id}/delete`)}
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
              <Translate contentKey="lawyerApp.lawyer.home.notFound">No Lawyers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Lawyer;
