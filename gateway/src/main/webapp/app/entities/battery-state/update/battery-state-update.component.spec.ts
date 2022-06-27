import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BatteryStateService } from '../service/battery-state.service';
import { IBatteryState, BatteryState } from '../battery-state.model';

import { BatteryStateUpdateComponent } from './battery-state-update.component';

describe('BatteryState Management Update Component', () => {
  let comp: BatteryStateUpdateComponent;
  let fixture: ComponentFixture<BatteryStateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let batteryStateService: BatteryStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BatteryStateUpdateComponent],
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
      .overrideTemplate(BatteryStateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BatteryStateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    batteryStateService = TestBed.inject(BatteryStateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const batteryState: IBatteryState = { id: 456 };

      activatedRoute.data = of({ batteryState });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(batteryState));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BatteryState>>();
      const batteryState = { id: 123 };
      jest.spyOn(batteryStateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ batteryState });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: batteryState }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(batteryStateService.update).toHaveBeenCalledWith(batteryState);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BatteryState>>();
      const batteryState = new BatteryState();
      jest.spyOn(batteryStateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ batteryState });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: batteryState }));
      saveSubject.complete();

      // THEN
      expect(batteryStateService.create).toHaveBeenCalledWith(batteryState);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BatteryState>>();
      const batteryState = { id: 123 };
      jest.spyOn(batteryStateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ batteryState });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(batteryStateService.update).toHaveBeenCalledWith(batteryState);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
