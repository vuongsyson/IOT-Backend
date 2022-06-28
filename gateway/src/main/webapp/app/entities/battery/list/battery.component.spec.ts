import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BatteryService } from '../service/battery.service';

import { BatteryComponent } from './battery.component';

describe('Battery Management Component', () => {
  let comp: BatteryComponent;
  let fixture: ComponentFixture<BatteryComponent>;
  let service: BatteryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BatteryComponent],
    })
      .overrideTemplate(BatteryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BatteryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BatteryService);

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
    expect(comp.batteries?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
