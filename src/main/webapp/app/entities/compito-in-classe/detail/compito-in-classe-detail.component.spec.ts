import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CompitoInClasseDetailComponent } from './compito-in-classe-detail.component';

describe('CompitoInClasse Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompitoInClasseDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CompitoInClasseDetailComponent,
              resolve: { compitoInClasse: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CompitoInClasseDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load compitoInClasse on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CompitoInClasseDetailComponent);

      // THEN
      expect(instance.compitoInClasse).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
