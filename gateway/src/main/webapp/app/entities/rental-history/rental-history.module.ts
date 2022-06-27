import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RentalHistoryComponent } from './list/rental-history.component';
import { RentalHistoryDetailComponent } from './detail/rental-history-detail.component';
import { RentalHistoryUpdateComponent } from './update/rental-history-update.component';
import { RentalHistoryDeleteDialogComponent } from './delete/rental-history-delete-dialog.component';
import { RentalHistoryRoutingModule } from './route/rental-history-routing.module';

@NgModule({
  imports: [SharedModule, RentalHistoryRoutingModule],
  declarations: [RentalHistoryComponent, RentalHistoryDetailComponent, RentalHistoryUpdateComponent, RentalHistoryDeleteDialogComponent],
  entryComponents: [RentalHistoryDeleteDialogComponent],
})
export class RentalHistoryModule {}
