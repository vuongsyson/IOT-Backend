import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CabinetDetailComponent } from './cabinet-detail.component';

describe('Cabinet Management Detail Component', () => {
  let comp: CabinetDetailComponent;
  let fixture: ComponentFixture<CabinetDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CabinetDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cabinet: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CabinetDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CabinetDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cabinet on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cabinet).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
