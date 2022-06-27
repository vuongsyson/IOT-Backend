import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CabinetService } from '../service/cabinet.service';

import { CabinetComponent } from './cabinet.component';

describe('Cabinet Management Component', () => {
  let comp: CabinetComponent;
  let fixture: ComponentFixture<CabinetComponent>;
  let service: CabinetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CabinetComponent],
    })
      .overrideTemplate(CabinetComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CabinetComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CabinetService);

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
    expect(comp.cabinets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
