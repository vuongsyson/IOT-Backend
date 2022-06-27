import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'battery',
        data: { pageTitle: 'gatewayApp.battery.home.title' },
        loadChildren: () => import('./battery/battery.module').then(m => m.BatteryModule),
      },
      {
        path: 'battery-state',
        data: { pageTitle: 'gatewayApp.batteryState.home.title' },
        loadChildren: () => import('./battery-state/battery-state.module').then(m => m.BatteryStateModule),
      },
      {
        path: 'bp-swap-record',
        data: { pageTitle: 'gatewayApp.bpSwapRecord.home.title' },
        loadChildren: () => import('./bp-swap-record/bp-swap-record.module').then(m => m.BpSwapRecordModule),
      },
      {
        path: 'bss',
        data: { pageTitle: 'gatewayApp.bss.home.title' },
        loadChildren: () => import('./bss/bss.module').then(m => m.BssModule),
      },
      {
        path: 'cabinet',
        data: { pageTitle: 'gatewayApp.cabinet.home.title' },
        loadChildren: () => import('./cabinet/cabinet.module').then(m => m.CabinetModule),
      },
      {
        path: 'device-type',
        data: { pageTitle: 'gatewayApp.deviceType.home.title' },
        loadChildren: () => import('./device-type/device-type.module').then(m => m.DeviceTypeModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'gatewayApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'group',
        data: { pageTitle: 'gatewayApp.group.home.title' },
        loadChildren: () => import('./group/group.module').then(m => m.GroupModule),
      },
      {
        path: 'org',
        data: { pageTitle: 'gatewayApp.org.home.title' },
        loadChildren: () => import('./org/org.module').then(m => m.OrgModule),
      },
      {
        path: 'refresh-token',
        data: { pageTitle: 'gatewayApp.refreshToken.home.title' },
        loadChildren: () => import('./refresh-token/refresh-token.module').then(m => m.RefreshTokenModule),
      },
      {
        path: 'rental-history',
        data: { pageTitle: 'gatewayApp.rentalHistory.home.title' },
        loadChildren: () => import('./rental-history/rental-history.module').then(m => m.RentalHistoryModule),
      },
      {
        path: 'sos-request',
        data: { pageTitle: 'gatewayApp.sosRequest.home.title' },
        loadChildren: () => import('./sos-request/sos-request.module').then(m => m.SosRequestModule),
      },
      {
        path: 'sos-type',
        data: { pageTitle: 'gatewayApp.sosType.home.title' },
        loadChildren: () => import('./sos-type/sos-type.module').then(m => m.SosTypeModule),
      },
      {
        path: 'user-auth',
        data: { pageTitle: 'gatewayApp.userAuth.home.title' },
        loadChildren: () => import('./user-auth/user-auth.module').then(m => m.UserAuthModule),
      },
      {
        path: 'vehicle',
        data: { pageTitle: 'gatewayApp.vehicle.home.title' },
        loadChildren: () => import('./vehicle/vehicle.module').then(m => m.VehicleModule),
      },
      {
        path: 'vehicle-state',
        data: { pageTitle: 'gatewayApp.vehicleState.home.title' },
        loadChildren: () => import('./vehicle-state/vehicle-state.module').then(m => m.VehicleStateModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
