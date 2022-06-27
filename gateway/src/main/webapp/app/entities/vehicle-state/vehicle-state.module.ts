import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VehicleStateComponent } from './list/vehicle-state.component';
import { VehicleStateDetailComponent } from './detail/vehicle-state-detail.component';
import { VehicleStateUpdateComponent } from './update/vehicle-state-update.component';
import { VehicleStateDeleteDialogComponent } from './delete/vehicle-state-delete-dialog.component';
import { VehicleStateRoutingModule } from './route/vehicle-state-routing.module';

@NgModule({
  imports: [SharedModule, VehicleStateRoutingModule],
  declarations: [VehicleStateComponent, VehicleStateDetailComponent, VehicleStateUpdateComponent, VehicleStateDeleteDialogComponent],
  entryComponents: [VehicleStateDeleteDialogComponent],
})
export class VehicleStateModule {}
