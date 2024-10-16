import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'history',
    data: { pageTitle: 'Histories' },
    loadChildren: () => import('./history/history.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
