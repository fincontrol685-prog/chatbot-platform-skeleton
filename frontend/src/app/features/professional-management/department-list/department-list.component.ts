import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfessionalManagementService } from '../professional-management.service';
import { DepartmentDto } from '../models/department.model';

@Component({
  selector: 'app-department-list',
  templateUrl: './department-list.component.html',
  styles: [`
    .container { padding: 20px; }
    h2 { margin-bottom: 20px; color: #1976d2; }
    .search-section { display: flex; gap: 10px; margin-bottom: 20px; align-items: center; }
    .search-field { flex: 1; max-width: 400px; }
    .loading { display: flex; justify-content: center; padding: 20px; }
    .empty-state { text-align: center; padding: 40px 20px; color: #666; }
    .empty-state mat-icon { font-size: 48px; width: 48px; height: 48px; margin-bottom: 10px; opacity: 0.3; }
    .detail-card { margin-top: 20px; margin-left: 20px; width: 90%; max-width: 500px; }
    .form-card { margin-top: 20px; margin-left: 20px; width: 90%; max-width: 600px; }
    .text-secondary { color: #999; font-size: 12px; }
    mat-list { max-width: 800px; }
    mat-list-item { cursor: pointer; margin-bottom: 5px; }
    mat-list-item:hover { background-color: #f5f5f5; }
  `]
})
export class DepartmentListComponent implements OnInit {
  departments: DepartmentDto[] = [];
  filteredDepartments: DepartmentDto[] = [];
  searchQuery = '';
  loading = false;
  error: string | null = null;
  selectedDepartment: DepartmentDto | null = null;
  showForm = false;

  constructor(private service: ProfessionalManagementService) {}

  ngOnInit(): void {
    this.loadDepartments();
  }

  loadDepartments(): void {
    this.loading = true;
    this.error = null;
    this.service.listDepartments().subscribe({
      next: (data) => {
        this.departments = data;
        this.filteredDepartments = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar departamentos: ' + err.message;
        this.loading = false;
      }
    });
  }

  search(): void {
    if (this.searchQuery.trim()) {
      this.loading = true;
      this.service.searchDepartments(this.searchQuery).subscribe({
        next: (data) => {
          this.filteredDepartments = data;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Erro ao buscar departamentos: ' + err.message;
          this.loading = false;
        }
      });
    } else {
      this.filteredDepartments = this.departments;
    }
  }

  selectDepartment(dept: DepartmentDto): void {
    this.selectedDepartment = dept;
  }

  openForm(): void {
    this.showForm = true;
    this.selectedDepartment = null;
  }

  closeForm(): void {
    this.showForm = false;
  }

  onDepartmentSaved(dept: DepartmentDto): void {
    this.loadDepartments();
    this.closeForm();
  }

  deleteDepartment(id: number | undefined): void {
    if (id && confirm('Tem certeza que deseja desativar este departamento?')) {
      this.service.deactivateDepartment(id).subscribe({
        next: () => {
          this.loadDepartments();
          this.selectedDepartment = null;
        },
        error: (err) => {
          this.error = 'Erro ao desativar departamento: ' + err.message;
        }
      });
    }
  }
}

