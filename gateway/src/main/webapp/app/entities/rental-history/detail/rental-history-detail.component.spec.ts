import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RentalHistoryDetailComponent } from './rental-history-detail.component';

describe('RentalHistory Management Detail Component', () => {
  let comp: RentalHistoryDetailComponent;
  let fixture: ComponentFixture<RentalHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RentalHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rentalHistory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RentalHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RentalHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rentalHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rentalHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
