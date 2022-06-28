import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RefreshTokenDetailComponent } from './refresh-token-detail.component';

describe('RefreshToken Management Detail Component', () => {
  let comp: RefreshTokenDetailComponent;
  let fixture: ComponentFixture<RefreshTokenDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RefreshTokenDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ refreshToken: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RefreshTokenDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RefreshTokenDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load refreshToken on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.refreshToken).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
