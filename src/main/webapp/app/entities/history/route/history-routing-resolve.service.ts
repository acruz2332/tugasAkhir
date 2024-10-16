import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistory } from '../history.model';
import { HistoryService } from '../service/history.service';

export const historyResolve = (route: ActivatedRouteSnapshot): Observable<null | IHistory> => {
  const id = route.params['id'];
  if (id) {
    return inject(HistoryService)
      .find(id)
      .pipe(
        mergeMap((history: HttpResponse<IHistory>) => {
          if (history.body) {
            return of(history.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default historyResolve;
