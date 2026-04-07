import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';
import { TwoFactorSetupComponent } from './two-factor-setup/two-factor-setup.component';
import { ConsentManagerComponent } from './consent-manager/consent-manager.component';
import { ComplianceSecurityService } from './compliance-security.service';

@NgModule({
  declarations: [
    TwoFactorSetupComponent,
    ConsentManagerComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    RouterModule.forChild([
      { path: '', redirectTo: 'security', pathMatch: 'full' },
      { path: 'security', component: TwoFactorSetupComponent },
      { path: 'consent', component: ConsentManagerComponent }
    ])
  ],
  providers: [ComplianceSecurityService]
})
export class ComplianceSecurityModule { }

