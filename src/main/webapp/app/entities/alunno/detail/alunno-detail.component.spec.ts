import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AlunnoDetailComponent } from './alunno-detail.component';

describe('Alunno Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlunnoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AlunnoDetailComponent,
              resolve: { alunno: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AlunnoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load alunno on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AlunnoDetailComponent);

      // THEN
      expect(instance.alunno).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
