import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BatteryStateComponent } from './list/battery-state.component';
import { BatteryStateDetailComponent } from './detail/battery-state-detail.component';
import { BatteryStateUpdateComponent } from './update/battery-state-update.component';
import { BatteryStateDeleteDialogComponent } from './delete/battery-state-delete-dialog.component';
import { BatteryStateRoutingModule } from './route/battery-state-routing.module';

@NgModule({
  imports: [SharedModule, BatteryStateRoutingModule],
  declarations: [BatteryStateComponent, BatteryStateDetailComponent, BatteryStateUpdateComponent, BatteryStateDeleteDialogComponent],
  entryComponents: [BatteryStateDeleteDialogComponent],
})
export class BatteryStateModule {}
