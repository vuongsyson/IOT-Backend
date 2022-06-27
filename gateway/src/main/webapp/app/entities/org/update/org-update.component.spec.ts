import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrgService } from '../service/org.service';
import { IOrg, Org } from '../org.model';

import { OrgUpdateComponent } from './org-update.component';

describe('Org Management Update Component', () => {
  let comp: OrgUpdateComponent;
  let fixture: ComponentFixture<OrgUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orgService: OrgService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrgUpdateComponent],
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
      .overrideTemplate(OrgUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orgService = TestBed.inject(OrgService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const org: IOrg = { id: 456 };

      activatedRoute.data = of({ org });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(org));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Org>>();
      const org = { id: 123 };
      jest.spyOn(orgService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ org });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: org }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(orgService.update).toHaveBeenCalledWith(org);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Org>>();
      const org = new Org();
      jest.spyOn(orgService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ org });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: org }));
      saveSubject.complete();

      // THEN
      expect(orgService.create).toHaveBeenCalledWith(org);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Org>>();
      const org = { id: 123 };
      jest.spyOn(orgService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ org });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orgService.update).toHaveBeenCalledWith(org);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
