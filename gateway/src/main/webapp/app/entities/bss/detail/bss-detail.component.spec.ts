import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BssDetailComponent } from './bss-detail.component';

describe('Bss Management Detail Component', () => {
  let comp: BssDetailComponent;
  let fixture: ComponentFixture<BssDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BssDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bss: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BssDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BssDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bss on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bss).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
