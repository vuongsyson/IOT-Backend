import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISosType } from '../sos-type.model';
import { SosTypeService } from '../service/sos-type.service';
import { SosTypeDeleteDialogComponent } from '../delete/sos-type-delete-dialog.component';

@Component({
  selector: 'jhi-sos-type',
  templateUrl: './sos-type.component.html',
})
export class SosTypeComponent implements OnInit {
  sosTypes?: ISosType[];
  isLoading = false;

  constructor(protected sosTypeService: SosTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sosTypeService.query().subscribe({
      next: (res: HttpResponse<ISosType[]>) => {
        this.isLoading = false;
        this.sosTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISosType): number {
    return item.id!;
  }

  delete(sosType: ISosType): void {
    const modalRef = this.modalService.open(SosTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sosType = sosType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
