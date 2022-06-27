import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BssService } from '../service/bss.service';
import { IBss, Bss } from '../bss.model';

import { BssUpdateComponent } from './bss-update.component';

describe('Bss Management Update Component', () => {
  let comp: BssUpdateComponent;
  let fixture: ComponentFixture<BssUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bssService: BssService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BssUpdateComponent],
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
      .overrideTemplate(BssUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BssUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bssService = TestBed.inject(BssService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bss: IBss = { id: 456 };

      activatedRoute.data = of({ bss });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bss));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bss>>();
      const bss = { id: 123 };
      jest.spyOn(bssService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bss });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bss }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bssService.update).toHaveBeenCalledWith(bss);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bss>>();
      const bss = new Bss();
      jest.spyOn(bssService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bss });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bss }));
      saveSubject.complete();

      // THEN
      expect(bssService.create).toHaveBeenCalledWith(bss);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bss>>();
      const bss = { id: 123 };
      jest.spyOn(bssService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bss });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bssService.update).toHaveBeenCalledWith(bss);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
