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
import { UserListComponent } from './user-list/user-list.component';

@NgModule({
  declarations: [
    DepartmentListComponent,
    DepartmentFormComponent,
    TeamListComponent,
    TeamFormComponent,
    UserListComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    RouterModule.forChild([
      { path: '', redirectTo: 'users', pathMatch: 'full' },
      { path: 'users', component: UserListComponent },
      { path: 'departments', component: DepartmentListComponent },
      { path: 'teams', component: TeamListComponent }
    ])
  ],
  providers: [ProfessionalManagementService]
})
export class ProfessionalManagementModule { }
