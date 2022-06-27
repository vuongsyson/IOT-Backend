import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SosTypeService } from '../service/sos-type.service';
import { ISosType, SosType } from '../sos-type.model';

import { SosTypeUpdateComponent } from './sos-type-update.component';

describe('SosType Management Update Component', () => {
  let comp: SosTypeUpdateComponent;
  let fixture: ComponentFixture<SosTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sosTypeService: SosTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SosTypeUpdateComponent],
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
      .overrideTemplate(SosTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SosTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sosTypeService = TestBed.inject(SosTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sosType: ISosType = { id: 456 };

      activatedRoute.data = of({ sosType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sosType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SosType>>();
      const sosType = { id: 123 };
      jest.spyOn(sosTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sosType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sosType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sosTypeService.update).toHaveBeenCalledWith(sosType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SosType>>();
      const sosType = new SosType();
      jest.spyOn(sosTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sosType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sosType }));
      saveSubject.complete();

      // THEN
      expect(sosTypeService.create).toHaveBeenCalledWith(sosType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SosType>>();
      const sosType = { id: 123 };
      jest.spyOn(sosTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sosType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sosTypeService.update).toHaveBeenCalledWith(sosType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
