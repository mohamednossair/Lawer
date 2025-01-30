import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { Route } from 'react-router';
import CaseDocument from './case-document';
import CaseSession from './case-session';
import CaseStatus from './case-status';
import Client from './client';
import Court from './court';
import CourtCase from './court-case';
import CourtCaseType from './court-case-type';
import Lawyer from './lawyer';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="case-document/*" element={<CaseDocument />} />
        <Route path="case-session/*" element={<CaseSession />} />
        <Route path="case-status/*" element={<CaseStatus />} />
        <Route path="client/*" element={<Client />} />
        <Route path="court/*" element={<Court />} />
        <Route path="court-case/*" element={<CourtCase />} />
        <Route path="court-case-type/*" element={<CourtCaseType />} />
        <Route path="lawyer/*" element={<Lawyer />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
