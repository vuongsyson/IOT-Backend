import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BatteryService } from '../service/battery.service';
import { IBattery, Battery } from '../battery.model';
import { IRentalHistory } from 'app/entities/rental-history/rental-history.model';
import { RentalHistoryService } from 'app/entities/rental-history/service/rental-history.service';

import { BatteryUpdateComponent } from './battery-update.component';

describe('Battery Management Update Component', () => {
  let comp: BatteryUpdateComponent;
  let fixture: ComponentFixture<BatteryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let batteryService: BatteryService;
  let rentalHistoryService: RentalHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BatteryUpdateComponent],
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
      .overrideTemplate(BatteryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BatteryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    batteryService = TestBed.inject(BatteryService);
    rentalHistoryService = TestBed.inject(RentalHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RentalHistory query and add missing value', () => {
      const battery: IBattery = { id: 456 };
      const rentalHistory: IRentalHistory = { id: 23801 };
      battery.rentalHistory = rentalHistory;

      const rentalHistoryCollection: IRentalHistory[] = [{ id: 14323 }];
      jest.spyOn(rentalHistoryService, 'query').mockReturnValue(of(new HttpResponse({ body: rentalHistoryCollection })));
      const additionalRentalHistories = [rentalHistory];
      const expectedCollection: IRentalHistory[] = [...additionalRentalHistories, ...rentalHistoryCollection];
      jest.spyOn(rentalHistoryService, 'addRentalHistoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ battery });
      comp.ngOnInit();

      expect(rentalHistoryService.query).toHaveBeenCalled();
      expect(rentalHistoryService.addRentalHistoryToCollectionIfMissing).toHaveBeenCalledWith(
        rentalHistoryCollection,
        ...additionalRentalHistories
      );
      expect(comp.rentalHistoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const battery: IBattery = { id: 456 };
      const rentalHistory: IRentalHistory = { id: 66743 };
      battery.rentalHistory = rentalHistory;

      activatedRoute.data = of({ battery });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(battery));
      expect(comp.rentalHistoriesSharedCollection).toContain(rentalHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Battery>>();
      const battery = { id: 123 };
      jest.spyOn(batteryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ battery });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: battery }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(batteryService.update).toHaveBeenCalledWith(battery);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Battery>>();
      const battery = new Battery();
      jest.spyOn(batteryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ battery });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: battery }));
      saveSubject.complete();

      // THEN
      expect(batteryService.create).toHaveBeenCalledWith(battery);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Battery>>();
      const battery = { id: 123 };
      jest.spyOn(batteryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ battery });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(batteryService.update).toHaveBeenCalledWith(battery);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRentalHistoryById', () => {
      it('Should return tracked RentalHistory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRentalHistoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
