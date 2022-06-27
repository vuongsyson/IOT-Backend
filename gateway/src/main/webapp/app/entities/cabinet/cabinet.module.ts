import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CabinetComponent } from './list/cabinet.component';
import { CabinetDetailComponent } from './detail/cabinet-detail.component';
import { CabinetUpdateComponent } from './update/cabinet-update.component';
import { CabinetDeleteDialogComponent } from './delete/cabinet-delete-dialog.component';
import { CabinetRoutingModule } from './route/cabinet-routing.module';

@NgModule({
  imports: [SharedModule, CabinetRoutingModule],
  declarations: [CabinetComponent, CabinetDetailComponent, CabinetUpdateComponent, CabinetDeleteDialogComponent],
  entryComponents: [CabinetDeleteDialogComponent],
})
export class CabinetModule {}
