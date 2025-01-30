import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CaseSession from './case-session';
import CaseSessionDetail from './case-session-detail';
import CaseSessionUpdate from './case-session-update';
import CaseSessionDeleteDialog from './case-session-delete-dialog';

const CaseSessionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CaseSession />} />
    <Route path="new" element={<CaseSessionUpdate />} />
    <Route path=":id">
      <Route index element={<CaseSessionDetail />} />
      <Route path="edit" element={<CaseSessionUpdate />} />
      <Route path="delete" element={<CaseSessionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CaseSessionRoutes;
