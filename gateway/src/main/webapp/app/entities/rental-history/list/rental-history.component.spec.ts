import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RentalHistoryService } from '../service/rental-history.service';

import { RentalHistoryComponent } from './rental-history.component';

describe('RentalHistory Management Component', () => {
  let comp: RentalHistoryComponent;
  let fixture: ComponentFixture<RentalHistoryComponent>;
  let service: RentalHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RentalHistoryComponent],
    })
      .overrideTemplate(RentalHistoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RentalHistoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RentalHistoryService);

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
    expect(comp.rentalHistories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
