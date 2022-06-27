import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BatteryStateDetailComponent } from './battery-state-detail.component';

describe('BatteryState Management Detail Component', () => {
  let comp: BatteryStateDetailComponent;
  let fixture: ComponentFixture<BatteryStateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BatteryStateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ batteryState: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BatteryStateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BatteryStateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load batteryState on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.batteryState).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
