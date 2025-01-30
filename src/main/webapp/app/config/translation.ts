import { Storage, TranslatorContext } from 'react-jhipster';

import { setLocale } from 'app/shared/reducers/locale';

TranslatorContext.setDefaultLocale('ar-ly');
TranslatorContext.setRenderInnerTextForMissingKeys(false);

export const languages: any = {
  'ar-ly': { name: 'العربية', rtl: true },
  en: { name: 'English' },
  // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
};

export const locales = Object.keys(languages).sort();

export const isRTL = (lang: string): boolean => languages[lang] && languages[lang].rtl;

export const setTextDirection = (lang: string) => {
  document.querySelector('html').setAttribute('dir', isRTL(lang) ? 'rtl' : 'ltr');
};

export const registerLocale = store => {
  store.dispatch(setLocale(Storage.session.get('locale', 'ar-ly')));
};
