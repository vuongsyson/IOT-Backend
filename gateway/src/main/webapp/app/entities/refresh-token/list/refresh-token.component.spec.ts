import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RefreshTokenService } from '../service/refresh-token.service';

import { RefreshTokenComponent } from './refresh-token.component';

describe('RefreshToken Management Component', () => {
  let comp: RefreshTokenComponent;
  let fixture: ComponentFixture<RefreshTokenComponent>;
  let service: RefreshTokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RefreshTokenComponent],
    })
      .overrideTemplate(RefreshTokenComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RefreshTokenComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RefreshTokenService);

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
    expect(comp.refreshTokens?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
