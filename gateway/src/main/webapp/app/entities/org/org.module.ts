import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrgComponent } from './list/org.component';
import { OrgDetailComponent } from './detail/org-detail.component';
import { OrgUpdateComponent } from './update/org-update.component';
import { OrgDeleteDialogComponent } from './delete/org-delete-dialog.component';
import { OrgRoutingModule } from './route/org-routing.module';

@NgModule({
  imports: [SharedModule, OrgRoutingModule],
  declarations: [OrgComponent, OrgDetailComponent, OrgUpdateComponent, OrgDeleteDialogComponent],
  entryComponents: [OrgDeleteDialogComponent],
})
export class OrgModule {}
