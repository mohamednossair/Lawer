import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CourtCase from './court-case';
import CourtCaseDetail from './court-case-detail';
import CourtCaseUpdate from './court-case-update';
import CourtCaseDeleteDialog from './court-case-delete-dialog';

const CourtCaseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CourtCase />} />
    <Route path="new" element={<CourtCaseUpdate />} />
    <Route path=":id">
      <Route index element={<CourtCaseDetail />} />
      <Route path="edit" element={<CourtCaseUpdate />} />
      <Route path="delete" element={<CourtCaseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CourtCaseRoutes;
