import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserAuthComponent } from './list/user-auth.component';
import { UserAuthDetailComponent } from './detail/user-auth-detail.component';
import { UserAuthUpdateComponent } from './update/user-auth-update.component';
import { UserAuthDeleteDialogComponent } from './delete/user-auth-delete-dialog.component';
import { UserAuthRoutingModule } from './route/user-auth-routing.module';

@NgModule({
  imports: [SharedModule, UserAuthRoutingModule],
  declarations: [UserAuthComponent, UserAuthDetailComponent, UserAuthUpdateComponent, UserAuthDeleteDialogComponent],
  entryComponents: [UserAuthDeleteDialogComponent],
})
export class UserAuthModule {}
