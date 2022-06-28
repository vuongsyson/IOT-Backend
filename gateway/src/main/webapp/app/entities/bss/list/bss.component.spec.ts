import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BssService } from '../service/bss.service';

import { BssComponent } from './bss.component';

describe('Bss Management Component', () => {
  let comp: BssComponent;
  let fixture: ComponentFixture<BssComponent>;
  let service: BssService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BssComponent],
    })
      .overrideTemplate(BssComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BssComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BssService);

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
    expect(comp.bsses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
