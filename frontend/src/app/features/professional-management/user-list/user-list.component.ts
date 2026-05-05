import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { getApiErrorMessage } from '../../../core/api-error.util';
import { DepartmentDto } from '../models/department.model';
import { RoleOption, UserProfile, UserUpsertRequest } from '../models/user-profile.model';
import { ProfessionalManagementService } from '../professional-management.service';

type StatusFilter = 'ALL' | 'ACTIVE' | 'INACTIVE';

@Component({
    selector: 'app-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.css'],
    standalone: false
})
export class UserListComponent implements OnInit {
  private readonly fb = inject(FormBuilder);

  readonly displayedColumns = ['user', 'roles', 'departments', 'status', 'createdAt', 'actions'];
  readonly form = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.maxLength(80)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(160)]],
    password: ['', [Validators.maxLength(120)]],
    enabled: [true],
    roles: this.fb.nonNullable.control<string[]>(['USUARIO']),
    departmentIds: this.fb.nonNullable.control<number[]>([])
  });

  users: UserProfile[] = [];
  roles: RoleOption[] = [];
  departments: DepartmentDto[] = [];
  loading = true;
  saving = false;
  error = '';
  search = '';
  statusFilter: StatusFilter = 'ALL';
  selectedUser: UserProfile | null = null;
  editing = false;

  constructor(
    private readonly professionalService: ProfessionalManagementService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  get filteredUsers(): UserProfile[] {
    const searchValue = this.search.trim().toLowerCase();
    return this.users.filter(user => {
      const matchesSearch = searchValue
        ? [
            user.username,
            user.displayName,
            user.email,
            ...user.roles,
            ...user.departmentNames
          ].some(value => value?.toLowerCase().includes(searchValue))
        : true;

      const matchesStatus =
        this.statusFilter === 'ALL'
          ? true
          : this.statusFilter === 'ACTIVE'
            ? user.enabled
            : !user.enabled;

      return matchesSearch && matchesStatus;
    });
  }

  get totalUsers(): number {
    return this.users.length;
  }

  get activeUsers(): number {
    return this.users.filter(user => user.enabled).length;
  }

  get uniqueRoles(): number {
    return new Set(this.users.flatMap(user => user.roles)).size;
  }

  get mappedDepartments(): number {
    return new Set(this.users.flatMap(user => user.departmentNames)).size;
  }

  loadData(): void {
    this.loading = true;
    this.error = '';

    forkJoin({
      users: this.professionalService.listUsers(),
      roles: this.professionalService.listRoles(),
      departments: this.professionalService.listDepartments()
    }).subscribe({
      next: ({ users, roles, departments }) => {
        this.users = users;
        this.roles = roles;
        this.departments = departments.filter(department => department.isActive !== false);
        this.loading = false;

        if (this.selectedUser) {
          this.selectedUser = this.users.find(user => user.id === this.selectedUser?.id) ?? null;
        }
      },
      error: err => {
        this.error = getApiErrorMessage(err, 'Nao foi possivel carregar usuarios e perfis.');
        this.loading = false;
      }
    });
  }

  selectUser(user: UserProfile): void {
    this.selectedUser = user;
    this.editing = false;
    this.form.reset({
      username: user.username,
      email: user.email,
      password: '',
      enabled: user.enabled,
      roles: [...user.roles],
      departmentIds: [...user.departmentIds]
    });
  }

  openCreate(): void {
    this.selectedUser = null;
    this.editing = true;
    this.form.reset({
      username: '',
      email: '',
      password: '',
      enabled: true,
      roles: ['USUARIO'],
      departmentIds: []
    });
    this.error = '';
  }

  openEdit(): void {
    if (!this.selectedUser) {
      return;
    }

    this.editing = true;
    this.form.reset({
      username: this.selectedUser.username,
      email: this.selectedUser.email,
      password: '',
      enabled: this.selectedUser.enabled,
      roles: [...this.selectedUser.roles],
      departmentIds: [...this.selectedUser.departmentIds]
    });
    this.error = '';
  }

  cancelEdit(): void {
    this.editing = false;

    if (this.selectedUser) {
      this.selectUser(this.selectedUser);
      return;
    }

    this.form.reset({
      username: '',
      email: '',
      password: '',
      enabled: true,
      roles: ['USUARIO'],
      departmentIds: []
    });
  }

  save(): void {
    this.error = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = this.buildPayload();
    if (!this.selectedUser && !payload.password?.trim()) {
      this.error = 'Informe uma senha inicial para criar o usuario.';
      return;
    }

    this.saving = true;
    const request = this.selectedUser
      ? this.professionalService.updateUser(this.selectedUser.id, payload)
      : this.professionalService.createUser(payload);

    request.subscribe({
      next: savedUser => {
        this.saving = false;
        this.editing = false;
        this.loadData();
        this.selectedUser = savedUser;
      },
      error: err => {
        this.error = getApiErrorMessage(err, 'Nao foi possivel salvar o usuario.');
        this.saving = false;
      }
    });
  }

  toggleStatus(user: UserProfile): void {
    this.professionalService.updateUserStatus(user.id, !user.enabled).subscribe({
      next: updatedUser => {
        this.users = this.users.map(item => item.id === updatedUser.id ? updatedUser : item);
        if (this.selectedUser?.id === updatedUser.id) {
          this.selectedUser = updatedUser;
        }
      },
      error: err => {
        this.error = getApiErrorMessage(err, 'Nao foi possivel atualizar o status do usuario.');
      }
    });
  }

  trackByUser(index: number, user: UserProfile): number {
    return user.id;
  }

  private buildPayload(): UserUpsertRequest {
    const value = this.form.getRawValue();
    return {
      username: value.username.trim(),
      email: value.email.trim(),
      password: value.password.trim() || undefined,
      enabled: value.enabled,
      roles: value.roles,
      departmentIds: value.departmentIds
    };
  }
}
