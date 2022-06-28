import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SosRequestComponent } from './list/sos-request.component';
import { SosRequestDetailComponent } from './detail/sos-request-detail.component';
import { SosRequestUpdateComponent } from './update/sos-request-update.component';
import { SosRequestDeleteDialogComponent } from './delete/sos-request-delete-dialog.component';
import { SosRequestRoutingModule } from './route/sos-request-routing.module';

@NgModule({
  imports: [SharedModule, SosRequestRoutingModule],
  declarations: [SosRequestComponent, SosRequestDetailComponent, SosRequestUpdateComponent, SosRequestDeleteDialogComponent],
  entryComponents: [SosRequestDeleteDialogComponent],
})
export class SosRequestModule {}
