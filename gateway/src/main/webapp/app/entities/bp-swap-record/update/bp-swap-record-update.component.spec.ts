import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BpSwapRecordService } from '../service/bp-swap-record.service';
import { IBpSwapRecord, BpSwapRecord } from '../bp-swap-record.model';

import { BpSwapRecordUpdateComponent } from './bp-swap-record-update.component';

describe('BpSwapRecord Management Update Component', () => {
  let comp: BpSwapRecordUpdateComponent;
  let fixture: ComponentFixture<BpSwapRecordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bpSwapRecordService: BpSwapRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BpSwapRecordUpdateComponent],
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
      .overrideTemplate(BpSwapRecordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BpSwapRecordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bpSwapRecordService = TestBed.inject(BpSwapRecordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bpSwapRecord: IBpSwapRecord = { id: 456 };

      activatedRoute.data = of({ bpSwapRecord });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bpSwapRecord));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BpSwapRecord>>();
      const bpSwapRecord = { id: 123 };
      jest.spyOn(bpSwapRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bpSwapRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bpSwapRecord }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bpSwapRecordService.update).toHaveBeenCalledWith(bpSwapRecord);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BpSwapRecord>>();
      const bpSwapRecord = new BpSwapRecord();
      jest.spyOn(bpSwapRecordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bpSwapRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bpSwapRecord }));
      saveSubject.complete();

      // THEN
      expect(bpSwapRecordService.create).toHaveBeenCalledWith(bpSwapRecord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BpSwapRecord>>();
      const bpSwapRecord = { id: 123 };
      jest.spyOn(bpSwapRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bpSwapRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bpSwapRecordService.update).toHaveBeenCalledWith(bpSwapRecord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
