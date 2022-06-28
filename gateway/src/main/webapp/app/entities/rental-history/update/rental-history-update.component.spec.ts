import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RentalHistoryService } from '../service/rental-history.service';
import { IRentalHistory, RentalHistory } from '../rental-history.model';

import { RentalHistoryUpdateComponent } from './rental-history-update.component';

describe('RentalHistory Management Update Component', () => {
  let comp: RentalHistoryUpdateComponent;
  let fixture: ComponentFixture<RentalHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rentalHistoryService: RentalHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RentalHistoryUpdateComponent],
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
      .overrideTemplate(RentalHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RentalHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rentalHistoryService = TestBed.inject(RentalHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rentalHistory: IRentalHistory = { id: 456 };

      activatedRoute.data = of({ rentalHistory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rentalHistory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RentalHistory>>();
      const rentalHistory = { id: 123 };
      jest.spyOn(rentalHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rentalHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rentalHistory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rentalHistoryService.update).toHaveBeenCalledWith(rentalHistory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RentalHistory>>();
      const rentalHistory = new RentalHistory();
      jest.spyOn(rentalHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rentalHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rentalHistory }));
      saveSubject.complete();

      // THEN
      expect(rentalHistoryService.create).toHaveBeenCalledWith(rentalHistory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RentalHistory>>();
      const rentalHistory = { id: 123 };
      jest.spyOn(rentalHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rentalHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rentalHistoryService.update).toHaveBeenCalledWith(rentalHistory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
