import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserAuthService } from '../service/user-auth.service';
import { IUserAuth, UserAuth } from '../user-auth.model';

import { UserAuthUpdateComponent } from './user-auth-update.component';

describe('UserAuth Management Update Component', () => {
  let comp: UserAuthUpdateComponent;
  let fixture: ComponentFixture<UserAuthUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userAuthService: UserAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserAuthUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UserAuthUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserAuthUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userAuthService = TestBed.inject(UserAuthService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userAuth: IUserAuth = { id: 456 };

      activatedRoute.data = of({ userAuth });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(userAuth));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserAuth>>();
      const userAuth = { id: 123 };
      jest.spyOn(userAuthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAuth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAuth }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(userAuthService.update).toHaveBeenCalledWith(userAuth);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserAuth>>();
      const userAuth = new UserAuth();
      jest.spyOn(userAuthService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAuth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAuth }));
      saveSubject.complete();

      // THEN
      expect(userAuthService.create).toHaveBeenCalledWith(userAuth);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserAuth>>();
      const userAuth = { id: 123 };
      jest.spyOn(userAuthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAuth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userAuthService.update).toHaveBeenCalledWith(userAuth);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
