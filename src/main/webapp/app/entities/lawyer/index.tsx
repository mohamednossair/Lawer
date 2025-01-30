import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Lawyer from './lawyer';
import LawyerDetail from './lawyer-detail';
import LawyerUpdate from './lawyer-update';
import LawyerDeleteDialog from './lawyer-delete-dialog';

const LawyerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Lawyer />} />
    <Route path="new" element={<LawyerUpdate />} />
    <Route path=":id">
      <Route index element={<LawyerDetail />} />
      <Route path="edit" element={<LawyerUpdate />} />
      <Route path="delete" element={<LawyerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LawyerRoutes;
