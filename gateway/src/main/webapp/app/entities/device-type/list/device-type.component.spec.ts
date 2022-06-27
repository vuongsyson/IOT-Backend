import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DeviceTypeService } from '../service/device-type.service';

import { DeviceTypeComponent } from './device-type.component';

describe('DeviceType Management Component', () => {
  let comp: DeviceTypeComponent;
  let fixture: ComponentFixture<DeviceTypeComponent>;
  let service: DeviceTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DeviceTypeComponent],
    })
      .overrideTemplate(DeviceTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DeviceTypeService);

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
    expect(comp.deviceTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
