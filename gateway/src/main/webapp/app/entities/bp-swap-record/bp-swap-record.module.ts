import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BpSwapRecordComponent } from './list/bp-swap-record.component';
import { BpSwapRecordDetailComponent } from './detail/bp-swap-record-detail.component';
import { BpSwapRecordUpdateComponent } from './update/bp-swap-record-update.component';
import { BpSwapRecordDeleteDialogComponent } from './delete/bp-swap-record-delete-dialog.component';
import { BpSwapRecordRoutingModule } from './route/bp-swap-record-routing.module';

@NgModule({
  imports: [SharedModule, BpSwapRecordRoutingModule],
  declarations: [BpSwapRecordComponent, BpSwapRecordDetailComponent, BpSwapRecordUpdateComponent, BpSwapRecordDeleteDialogComponent],
  entryComponents: [BpSwapRecordDeleteDialogComponent],
})
export class BpSwapRecordModule {}
