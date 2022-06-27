import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrgService } from '../service/org.service';

import { OrgComponent } from './org.component';

describe('Org Management Component', () => {
  let comp: OrgComponent;
  let fixture: ComponentFixture<OrgComponent>;
  let service: OrgService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OrgComponent],
    })
      .overrideTemplate(OrgComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrgService);

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
    expect(comp.orgs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
