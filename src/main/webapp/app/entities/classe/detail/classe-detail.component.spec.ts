import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClasseDetailComponent } from './classe-detail.component';

describe('Classe Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClasseDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ClasseDetailComponent,
              resolve: { classe: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClasseDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load classe on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClasseDetailComponent);

      // THEN
      expect(instance.classe).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
