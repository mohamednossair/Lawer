import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CaseDocument from './case-document';
import CaseDocumentDetail from './case-document-detail';
import CaseDocumentUpdate from './case-document-update';
import CaseDocumentDeleteDialog from './case-document-delete-dialog';

const CaseDocumentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CaseDocument />} />
    <Route path="new" element={<CaseDocumentUpdate />} />
    <Route path=":id">
      <Route index element={<CaseDocumentDetail />} />
      <Route path="edit" element={<CaseDocumentUpdate />} />
      <Route path="delete" element={<CaseDocumentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CaseDocumentRoutes;
