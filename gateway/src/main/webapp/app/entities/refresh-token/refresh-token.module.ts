import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RefreshTokenComponent } from './list/refresh-token.component';
import { RefreshTokenDetailComponent } from './detail/refresh-token-detail.component';
import { RefreshTokenUpdateComponent } from './update/refresh-token-update.component';
import { RefreshTokenDeleteDialogComponent } from './delete/refresh-token-delete-dialog.component';
import { RefreshTokenRoutingModule } from './route/refresh-token-routing.module';

@NgModule({
  imports: [SharedModule, RefreshTokenRoutingModule],
  declarations: [RefreshTokenComponent, RefreshTokenDetailComponent, RefreshTokenUpdateComponent, RefreshTokenDeleteDialogComponent],
  entryComponents: [RefreshTokenDeleteDialogComponent],
})
export class RefreshTokenModule {}
