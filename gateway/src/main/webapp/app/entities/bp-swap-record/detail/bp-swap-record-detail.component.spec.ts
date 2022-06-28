import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BpSwapRecordDetailComponent } from './bp-swap-record-detail.component';

describe('BpSwapRecord Management Detail Component', () => {
  let comp: BpSwapRecordDetailComponent;
  let fixture: ComponentFixture<BpSwapRecordDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BpSwapRecordDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bpSwapRecord: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BpSwapRecordDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BpSwapRecordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bpSwapRecord on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bpSwapRecord).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
