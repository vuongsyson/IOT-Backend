import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BatteryDetailComponent } from './battery-detail.component';

describe('Battery Management Detail Component', () => {
  let comp: BatteryDetailComponent;
  let fixture: ComponentFixture<BatteryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BatteryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ battery: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BatteryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BatteryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load battery on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.battery).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
