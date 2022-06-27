import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SosTypeComponent } from './list/sos-type.component';
import { SosTypeDetailComponent } from './detail/sos-type-detail.component';
import { SosTypeUpdateComponent } from './update/sos-type-update.component';
import { SosTypeDeleteDialogComponent } from './delete/sos-type-delete-dialog.component';
import { SosTypeRoutingModule } from './route/sos-type-routing.module';

@NgModule({
  imports: [SharedModule, SosTypeRoutingModule],
  declarations: [SosTypeComponent, SosTypeDetailComponent, SosTypeUpdateComponent, SosTypeDeleteDialogComponent],
  entryComponents: [SosTypeDeleteDialogComponent],
})
export class SosTypeModule {}
