import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeviceType } from '../device-type.model';
import { DeviceTypeService } from '../service/device-type.service';
import { DeviceTypeDeleteDialogComponent } from '../delete/device-type-delete-dialog.component';

@Component({
  selector: 'jhi-device-type',
  templateUrl: './device-type.component.html',
})
export class DeviceTypeComponent implements OnInit {
  deviceTypes?: IDeviceType[];
  isLoading = false;

  constructor(protected deviceTypeService: DeviceTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.deviceTypeService.query().subscribe({
      next: (res: HttpResponse<IDeviceType[]>) => {
        this.isLoading = false;
        this.deviceTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDeviceType): number {
    return item.id!;
  }

  delete(deviceType: IDeviceType): void {
    const modalRef = this.modalService.open(DeviceTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deviceType = deviceType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
