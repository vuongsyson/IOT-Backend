import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrgDetailComponent } from './org-detail.component';

describe('Org Management Detail Component', () => {
  let comp: OrgDetailComponent;
  let fixture: ComponentFixture<OrgDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrgDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ org: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrgDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrgDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load org on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.org).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
