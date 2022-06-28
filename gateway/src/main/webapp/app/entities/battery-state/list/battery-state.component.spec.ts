import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BatteryStateService } from '../service/battery-state.service';

import { BatteryStateComponent } from './battery-state.component';

describe('BatteryState Management Component', () => {
  let comp: BatteryStateComponent;
  let fixture: ComponentFixture<BatteryStateComponent>;
  let service: BatteryStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BatteryStateComponent],
    })
      .overrideTemplate(BatteryStateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BatteryStateComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BatteryStateService);

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
    expect(comp.batteryStates?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
