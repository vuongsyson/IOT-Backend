import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { VehicleStateService } from '../service/vehicle-state.service';

import { VehicleStateComponent } from './vehicle-state.component';

describe('VehicleState Management Component', () => {
  let comp: VehicleStateComponent;
  let fixture: ComponentFixture<VehicleStateComponent>;
  let service: VehicleStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VehicleStateComponent],
    })
      .overrideTemplate(VehicleStateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleStateComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VehicleStateService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.vehicleStates?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
