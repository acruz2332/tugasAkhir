import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { HistoryDetailComponent } from './history-detail.component';

describe('History Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoryDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: HistoryDetailComponent,
              resolve: { history: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HistoryDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load history on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HistoryDetailComponent);

      // THEN
      expect(instance.history).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
