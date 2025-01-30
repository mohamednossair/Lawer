import MenuItem from 'app/shared/layout/menus/menu-item';
import React from 'react';
import { Translate } from 'react-jhipster';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/case-document">
        <Translate contentKey="global.menu.entities.caseDocument" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/case-session">
        <Translate contentKey="global.menu.entities.caseSession" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/case-status">
        <Translate contentKey="global.menu.entities.caseStatus" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/client">
        <Translate contentKey="global.menu.entities.client" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/court">
        <Translate contentKey="global.menu.entities.court" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/court-case">
        <Translate contentKey="global.menu.entities.courtCase" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/court-case-type">
        <Translate contentKey="global.menu.entities.courtCaseType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/lawyer">
        <Translate contentKey="global.menu.entities.lawyer" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
