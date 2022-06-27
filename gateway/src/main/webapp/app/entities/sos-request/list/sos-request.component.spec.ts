import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SosRequestService } from '../service/sos-request.service';

import { SosRequestComponent } from './sos-request.component';

describe('SosRequest Management Component', () => {
  let comp: SosRequestComponent;
  let fixture: ComponentFixture<SosRequestComponent>;
  let service: SosRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SosRequestComponent],
    })
      .overrideTemplate(SosRequestComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SosRequestComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SosRequestService);

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
    expect(comp.sosRequests?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
