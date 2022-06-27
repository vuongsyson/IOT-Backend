import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SosRequestService } from '../service/sos-request.service';
import { ISosRequest, SosRequest } from '../sos-request.model';

import { SosRequestUpdateComponent } from './sos-request-update.component';

describe('SosRequest Management Update Component', () => {
  let comp: SosRequestUpdateComponent;
  let fixture: ComponentFixture<SosRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sosRequestService: SosRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SosRequestUpdateComponent],
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
      .overrideTemplate(SosRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SosRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sosRequestService = TestBed.inject(SosRequestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sosRequest: ISosRequest = { id: 456 };

      activatedRoute.data = of({ sosRequest });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sosRequest));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SosRequest>>();
      const sosRequest = { id: 123 };
      jest.spyOn(sosRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sosRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sosRequest }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sosRequestService.update).toHaveBeenCalledWith(sosRequest);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SosRequest>>();
      const sosRequest = new SosRequest();
      jest.spyOn(sosRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sosRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sosRequest }));
      saveSubject.complete();

      // THEN
      expect(sosRequestService.create).toHaveBeenCalledWith(sosRequest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SosRequest>>();
      const sosRequest = { id: 123 };
      jest.spyOn(sosRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sosRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sosRequestService.update).toHaveBeenCalledWith(sosRequest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
