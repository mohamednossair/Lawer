import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CourtCaseType from './court-case-type';
import CourtCaseTypeDetail from './court-case-type-detail';
import CourtCaseTypeUpdate from './court-case-type-update';
import CourtCaseTypeDeleteDialog from './court-case-type-delete-dialog';

const CourtCaseTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CourtCaseType />} />
    <Route path="new" element={<CourtCaseTypeUpdate />} />
    <Route path=":id">
      <Route index element={<CourtCaseTypeDetail />} />
      <Route path="edit" element={<CourtCaseTypeUpdate />} />
      <Route path="delete" element={<CourtCaseTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CourtCaseTypeRoutes;
