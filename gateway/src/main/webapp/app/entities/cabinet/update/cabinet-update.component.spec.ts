import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CabinetService } from '../service/cabinet.service';
import { ICabinet, Cabinet } from '../cabinet.model';

import { CabinetUpdateComponent } from './cabinet-update.component';

describe('Cabinet Management Update Component', () => {
  let comp: CabinetUpdateComponent;
  let fixture: ComponentFixture<CabinetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cabinetService: CabinetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CabinetUpdateComponent],
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
      .overrideTemplate(CabinetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CabinetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cabinetService = TestBed.inject(CabinetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cabinet: ICabinet = { id: 456 };

      activatedRoute.data = of({ cabinet });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cabinet));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cabinet>>();
      const cabinet = { id: 123 };
      jest.spyOn(cabinetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cabinet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cabinet }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cabinetService.update).toHaveBeenCalledWith(cabinet);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cabinet>>();
      const cabinet = new Cabinet();
      jest.spyOn(cabinetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cabinet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cabinet }));
      saveSubject.complete();

      // THEN
      expect(cabinetService.create).toHaveBeenCalledWith(cabinet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cabinet>>();
      const cabinet = { id: 123 };
      jest.spyOn(cabinetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cabinet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cabinetService.update).toHaveBeenCalledWith(cabinet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
