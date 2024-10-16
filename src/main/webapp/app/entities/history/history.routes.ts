import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { HistoryComponent } from './list/history.component';
import { HistoryDetailComponent } from './detail/history-detail.component';
import { HistoryUpdateComponent } from './update/history-update.component';
import HistoryResolve from './route/history-routing-resolve.service';

const historyRoute: Routes = [
  {
    path: '',
    component: HistoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HistoryDetailComponent,
    resolve: {
      history: HistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HistoryUpdateComponent,
    resolve: {
      history: HistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HistoryUpdateComponent,
    resolve: {
      history: HistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default historyRoute;
