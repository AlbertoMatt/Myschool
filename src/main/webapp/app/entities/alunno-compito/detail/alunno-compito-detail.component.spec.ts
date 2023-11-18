import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AlunnoCompitoDetailComponent } from './alunno-compito-detail.component';

describe('AlunnoCompito Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlunnoCompitoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AlunnoCompitoDetailComponent,
              resolve: { alunnoCompito: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AlunnoCompitoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load alunnoCompito on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AlunnoCompitoDetailComponent);

      // THEN
      expect(instance.alunnoCompito).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
