import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CaseStatus from './case-status';
import CaseStatusDetail from './case-status-detail';
import CaseStatusUpdate from './case-status-update';
import CaseStatusDeleteDialog from './case-status-delete-dialog';

const CaseStatusRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CaseStatus />} />
    <Route path="new" element={<CaseStatusUpdate />} />
    <Route path=":id">
      <Route index element={<CaseStatusDetail />} />
      <Route path="edit" element={<CaseStatusUpdate />} />
      <Route path="delete" element={<CaseStatusDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CaseStatusRoutes;
