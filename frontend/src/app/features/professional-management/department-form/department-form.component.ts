import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfessionalManagementService } from '../professional-management.service';
import { DepartmentDto } from '../models/department.model';

@Component({
    selector: 'app-department-form',
    templateUrl: './department-form.component.html',
    styles: [`
    .full-width { width: 100%; margin-bottom: 15px; }
    .form-actions { display: flex; gap: 10px; margin-top: 20px; }
    form { padding: 10px 0; }
    mat-spinner { margin-right: 8px; }
  `],
    standalone: false
})
export class DepartmentFormComponent implements OnInit {
  @Input() department: DepartmentDto | null = null;
  @Output() saved = new EventEmitter<DepartmentDto>();
  @Output() cancelled = new EventEmitter<void>();

  form: FormGroup;
  loading = false;
  error: string | null = null;
  departments: DepartmentDto[] = [];

  constructor(
    private fb: FormBuilder,
    private service: ProfessionalManagementService
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      code: ['', [Validators.required, Validators.minLength(2)]],
      location: [''],
      description: [''],
      parentDepartmentId: [null]
    });
  }

  ngOnInit(): void {
    this.initForm();
    this.loadDepartments();
  }

  initForm(): void {
    if (this.department) {
      this.form.patchValue(this.department);
    }
  }

  loadDepartments(): void {
    this.service.listDepartments().subscribe({
      next: (data) => {
        this.departments = data.filter(d => d.id !== this.department?.id);
      },
      error: () => {
        // Silently fail, departments are optional
      }
    });
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }

    this.loading = true;
    this.error = null;

    const data = this.form.value;
    const request = this.department
      ? this.service.updateDepartment(this.department.id!, data)
      : this.service.createDepartment(data);

    request.subscribe({
      next: (result) => {
        this.loading = false;
        this.saved.emit(result);
      },
      error: (err) => {
        this.error = 'Erro ao salvar departamento: ' + err.message;
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.cancelled.emit();
  }
}

