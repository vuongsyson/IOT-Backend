import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BatteryComponent } from './list/battery.component';
import { BatteryDetailComponent } from './detail/battery-detail.component';
import { BatteryUpdateComponent } from './update/battery-update.component';
import { BatteryDeleteDialogComponent } from './delete/battery-delete-dialog.component';
import { BatteryRoutingModule } from './route/battery-routing.module';

@NgModule({
  imports: [SharedModule, BatteryRoutingModule],
  declarations: [BatteryComponent, BatteryDetailComponent, BatteryUpdateComponent, BatteryDeleteDialogComponent],
  entryComponents: [BatteryDeleteDialogComponent],
})
export class BatteryModule {}
