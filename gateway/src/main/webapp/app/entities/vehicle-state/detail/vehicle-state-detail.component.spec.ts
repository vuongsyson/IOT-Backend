import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VehicleStateDetailComponent } from './vehicle-state-detail.component';

describe('VehicleState Management Detail Component', () => {
  let comp: VehicleStateDetailComponent;
  let fixture: ComponentFixture<VehicleStateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VehicleStateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vehicleState: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VehicleStateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VehicleStateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vehicleState on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vehicleState).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
