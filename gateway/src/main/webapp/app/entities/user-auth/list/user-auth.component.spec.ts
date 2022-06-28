import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UserAuthService } from '../service/user-auth.service';

import { UserAuthComponent } from './user-auth.component';

describe('UserAuth Management Component', () => {
  let comp: UserAuthComponent;
  let fixture: ComponentFixture<UserAuthComponent>;
  let service: UserAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [UserAuthComponent],
    })
      .overrideTemplate(UserAuthComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserAuthComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(UserAuthService);

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
    expect(comp.userAuths?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
