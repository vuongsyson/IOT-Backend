import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BssComponent } from './list/bss.component';
import { BssDetailComponent } from './detail/bss-detail.component';
import { BssUpdateComponent } from './update/bss-update.component';
import { BssDeleteDialogComponent } from './delete/bss-delete-dialog.component';
import { BssRoutingModule } from './route/bss-routing.module';

@NgModule({
  imports: [SharedModule, BssRoutingModule],
  declarations: [BssComponent, BssDetailComponent, BssUpdateComponent, BssDeleteDialogComponent],
  entryComponents: [BssDeleteDialogComponent],
})
export class BssModule {}
