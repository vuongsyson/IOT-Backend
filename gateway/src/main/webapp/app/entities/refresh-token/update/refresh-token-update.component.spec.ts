import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RefreshTokenService } from '../service/refresh-token.service';
import { IRefreshToken, RefreshToken } from '../refresh-token.model';

import { RefreshTokenUpdateComponent } from './refresh-token-update.component';

describe('RefreshToken Management Update Component', () => {
  let comp: RefreshTokenUpdateComponent;
  let fixture: ComponentFixture<RefreshTokenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let refreshTokenService: RefreshTokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RefreshTokenUpdateComponent],
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
      .overrideTemplate(RefreshTokenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RefreshTokenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    refreshTokenService = TestBed.inject(RefreshTokenService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const refreshToken: IRefreshToken = { id: 456 };

      activatedRoute.data = of({ refreshToken });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(refreshToken));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RefreshToken>>();
      const refreshToken = { id: 123 };
      jest.spyOn(refreshTokenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ refreshToken });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: refreshToken }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(refreshTokenService.update).toHaveBeenCalledWith(refreshToken);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RefreshToken>>();
      const refreshToken = new RefreshToken();
      jest.spyOn(refreshTokenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ refreshToken });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: refreshToken }));
      saveSubject.complete();

      // THEN
      expect(refreshTokenService.create).toHaveBeenCalledWith(refreshToken);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RefreshToken>>();
      const refreshToken = { id: 123 };
      jest.spyOn(refreshTokenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ refreshToken });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(refreshTokenService.update).toHaveBeenCalledWith(refreshToken);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
