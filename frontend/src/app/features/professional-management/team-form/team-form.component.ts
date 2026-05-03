import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfessionalManagementService } from '../professional-management.service';
import { TeamDto, DepartmentDto } from '../models/department.model';

@Component({
    selector: 'app-team-form',
    templateUrl: './team-form.component.html',
    styles: [`
    .full-width { width: 100%; margin-bottom: 15px; }
    .form-actions { display: flex; gap: 10px; margin-top: 20px; }
    form { padding: 10px 0; }
    mat-spinner { margin-right: 8px; }
  `],
    standalone: false
})
export class TeamFormComponent implements OnInit {
  @Input() team: TeamDto | null = null;
  @Output() saved = new EventEmitter<TeamDto>();
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
      description: [''],
      departmentId: [null, Validators.required],
      maxConversationsPerUser: [10, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.initForm();
    this.loadDepartments();
  }

  initForm(): void {
    if (this.team) {
      this.form.patchValue(this.team);
    }
  }

  loadDepartments(): void {
    this.service.listDepartments().subscribe({
      next: (data) => {
        this.departments = data;
      },
      error: () => {
        this.error = 'Erro ao carregar departamentos';
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
    const request = this.team
      ? this.service.updateTeam(this.team.id!, data)
      : this.service.createTeam(data);

    request.subscribe({
      next: (result) => {
        this.loading = false;
        this.saved.emit(result);
      },
      error: (err) => {
        this.error = 'Erro ao salvar equipe: ' + err.message;
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.cancelled.emit();
  }

  getDepartmentName(id: number | null): string {
    if (!id) return '';
    const dept = this.departments.find(d => d.id === id);
    return dept ? dept.name : '';
  }
}

