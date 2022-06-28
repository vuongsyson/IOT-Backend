import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BpSwapRecordService } from '../service/bp-swap-record.service';

import { BpSwapRecordComponent } from './bp-swap-record.component';

describe('BpSwapRecord Management Component', () => {
  let comp: BpSwapRecordComponent;
  let fixture: ComponentFixture<BpSwapRecordComponent>;
  let service: BpSwapRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BpSwapRecordComponent],
    })
      .overrideTemplate(BpSwapRecordComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BpSwapRecordComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BpSwapRecordService);

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
    expect(comp.bpSwapRecords?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
