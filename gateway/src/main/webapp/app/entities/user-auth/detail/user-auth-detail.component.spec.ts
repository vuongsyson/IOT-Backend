import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserAuthDetailComponent } from './user-auth-detail.component';

describe('UserAuth Management Detail Component', () => {
  let comp: UserAuthDetailComponent;
  let fixture: ComponentFixture<UserAuthDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserAuthDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ userAuth: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UserAuthDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UserAuthDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userAuth on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.userAuth).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
