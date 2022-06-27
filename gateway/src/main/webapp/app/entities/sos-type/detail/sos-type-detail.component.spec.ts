import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SosTypeDetailComponent } from './sos-type-detail.component';

describe('SosType Management Detail Component', () => {
  let comp: SosTypeDetailComponent;
  let fixture: ComponentFixture<SosTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SosTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sosType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SosTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SosTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sosType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sosType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
