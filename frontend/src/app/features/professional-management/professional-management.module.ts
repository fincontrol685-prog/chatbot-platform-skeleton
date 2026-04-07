import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';
import { DepartmentListComponent } from './department-list/department-list.component';
import { DepartmentFormComponent } from './department-form/department-form.component';
import { TeamListComponent } from './team-list/team-list.component';
import { TeamFormComponent } from './team-form/team-form.component';
import { ProfessionalManagementService } from './professional-management.service';

@NgModule({
  declarations: [
    DepartmentListComponent,
    DepartmentFormComponent,
    TeamListComponent,
    TeamFormComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    RouterModule.forChild([
      { path: '', redirectTo: 'departments', pathMatch: 'full' },
      { path: 'departments', component: DepartmentListComponent },
      { path: 'teams', component: TeamListComponent }
    ])
  ],
  providers: [ProfessionalManagementService]
})
export class ProfessionalManagementModule { }

