import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VehicleStateService } from '../service/vehicle-state.service';
import { IVehicleState, VehicleState } from '../vehicle-state.model';

import { VehicleStateUpdateComponent } from './vehicle-state-update.component';

describe('VehicleState Management Update Component', () => {
  let comp: VehicleStateUpdateComponent;
  let fixture: ComponentFixture<VehicleStateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleStateService: VehicleStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VehicleStateUpdateComponent],
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
      .overrideTemplate(VehicleStateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleStateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleStateService = TestBed.inject(VehicleStateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vehicleState: IVehicleState = { id: 456 };

      activatedRoute.data = of({ vehicleState });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vehicleState));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VehicleState>>();
      const vehicleState = { id: 123 };
      jest.spyOn(vehicleStateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleState });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleState }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleStateService.update).toHaveBeenCalledWith(vehicleState);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VehicleState>>();
      const vehicleState = new VehicleState();
      jest.spyOn(vehicleStateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleState });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleState }));
      saveSubject.complete();

      // THEN
      expect(vehicleStateService.create).toHaveBeenCalledWith(vehicleState);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VehicleState>>();
      const vehicleState = { id: 123 };
      jest.spyOn(vehicleStateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleState });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleStateService.update).toHaveBeenCalledWith(vehicleState);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
