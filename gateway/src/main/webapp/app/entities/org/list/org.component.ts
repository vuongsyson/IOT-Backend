import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrg } from '../org.model';
import { OrgService } from '../service/org.service';
import { OrgDeleteDialogComponent } from '../delete/org-delete-dialog.component';

@Component({
  selector: 'jhi-org',
  templateUrl: './org.component.html',
})
export class OrgComponent implements OnInit {
  orgs?: IOrg[];
  isLoading = false;

  constructor(protected orgService: OrgService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.orgService.query().subscribe({
      next: (res: HttpResponse<IOrg[]>) => {
        this.isLoading = false;
        this.orgs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IOrg): number {
    return item.id!;
  }

  delete(org: IOrg): void {
    const modalRef = this.modalService.open(OrgDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.org = org;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
