import caseDocument from 'app/entities/case-document/case-document.reducer';
import caseSession from 'app/entities/case-session/case-session.reducer';
import caseStatus from 'app/entities/case-status/case-status.reducer';
import client from 'app/entities/client/client.reducer';
import court from 'app/entities/court/court.reducer';
import courtCase from 'app/entities/court-case/court-case.reducer';
import courtCaseType from 'app/entities/court-case-type/court-case-type.reducer';
import lawyer from 'app/entities/lawyer/lawyer.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  caseDocument,
  caseSession,
  caseStatus,
  client,
  court,
  courtCase,
  courtCaseType,
  lawyer,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
