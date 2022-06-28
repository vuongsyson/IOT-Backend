import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SosRequestDetailComponent } from './sos-request-detail.component';

describe('SosRequest Management Detail Component', () => {
  let comp: SosRequestDetailComponent;
  let fixture: ComponentFixture<SosRequestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SosRequestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sosRequest: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SosRequestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SosRequestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sosRequest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sosRequest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
