import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { DepartmentDto, TeamDto, TeamMemberDto, DepartmentHierarchyDto } from './models/department.model';
import { RoleOption, UserProfile, UserUpsertRequest } from './models/user-profile.model';

interface PaginatedResponse<T> {
  content?: T[];
}

@Injectable({ providedIn: 'root' })
export class ProfessionalManagementService {
  private apiBase = environment.apiUrl || '';
  private deptApi = `${this.apiBase}/api/departments`;
  private teamApi = `${this.apiBase}/api/teams`;
  private userApi = `${this.apiBase}/api/users`;

  constructor(private http: HttpClient) {}

  // ===== DEPARTMENTS =====
  listDepartments(): Observable<DepartmentDto[]> {
    return this.http.get<DepartmentDto[] | PaginatedResponse<DepartmentDto>>(this.deptApi)
      .pipe(map(response => this.unwrapCollection(response)));
  }

  getRootDepartments(): Observable<DepartmentDto[]> {
    return this.http.get<DepartmentDto[]>(`${this.deptApi}/root`);
  }

  getDepartment(id: number): Observable<DepartmentDto> {
    return this.http.get<DepartmentDto>(`${this.deptApi}/${id}`);
  }

  getDepartmentHierarchy(id: number): Observable<DepartmentHierarchyDto> {
    return this.http.get<DepartmentHierarchyDto>(`${this.deptApi}/${id}/hierarchy`);
  }

  getSubdepartments(id: number): Observable<DepartmentDto[]> {
    return this.http.get<DepartmentDto[]>(`${this.deptApi}/${id}/subdepartments`);
  }

  searchDepartments(query: string): Observable<DepartmentDto[]> {
    const params = new HttpParams().set('q', query);
    return this.http.get<DepartmentDto[] | PaginatedResponse<DepartmentDto>>(`${this.deptApi}/search`, { params })
      .pipe(map(response => this.unwrapCollection(response)));
  }

  createDepartment(dept: Partial<DepartmentDto>): Observable<DepartmentDto> {
    return this.http.post<DepartmentDto>(this.deptApi, dept);
  }

  updateDepartment(id: number, dept: Partial<DepartmentDto>): Observable<DepartmentDto> {
    return this.http.put<DepartmentDto>(`${this.deptApi}/${id}`, dept);
  }

  deactivateDepartment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.deptApi}/${id}`);
  }

  // ===== TEAMS =====
  listTeams(): Observable<TeamDto[]> {
    return this.http.get<TeamDto[] | PaginatedResponse<TeamDto>>(this.teamApi)
      .pipe(map(response => this.unwrapCollection(response)));
  }

  getTeam(id: number): Observable<TeamDto> {
    return this.http.get<TeamDto>(`${this.teamApi}/${id}`);
  }

  getTeamsByDepartment(departmentId: number): Observable<TeamDto[]> {
    return this.http.get<TeamDto[] | PaginatedResponse<TeamDto>>(`${this.teamApi}/department/${departmentId}`)
      .pipe(map(response => this.unwrapCollection(response)));
  }

  searchTeams(query: string): Observable<TeamDto[]> {
    const params = new HttpParams().set('q', query);
    return this.http.get<TeamDto[] | PaginatedResponse<TeamDto>>(`${this.teamApi}/search`, { params })
      .pipe(map(response => this.unwrapCollection(response)));
  }

  createTeam(team: Partial<TeamDto>): Observable<TeamDto> {
    return this.http.post<TeamDto>(this.teamApi, team);
  }

  updateTeam(id: number, team: Partial<TeamDto>): Observable<TeamDto> {
    return this.http.put<TeamDto>(`${this.teamApi}/${id}`, team);
  }

  deactivateTeam(id: number): Observable<void> {
    return this.http.delete<void>(`${this.teamApi}/${id}`);
  }

  // ===== TEAM MEMBERS =====
  addTeamMember(teamId: number, userId: number): Observable<TeamMemberDto> {
    return this.http.post<TeamMemberDto>(`${this.teamApi}/${teamId}/members/${userId}`, {});
  }

  removeTeamMember(teamId: number, userId: number): Observable<void> {
    return this.http.delete<void>(`${this.teamApi}/${teamId}/members/${userId}`);
  }

  setTeamLead(teamId: number, userId: number): Observable<TeamDto> {
    return this.http.put<TeamDto>(`${this.teamApi}/${teamId}/lead/${userId}`, {});
  }

  getTeamMembers(teamId: number): Observable<TeamMemberDto[]> {
    return this.http.get<TeamMemberDto[]>(`${this.teamApi}/${teamId}/members`);
  }

  // ===== USERS =====
  listUsers(search?: string, enabled?: boolean): Observable<UserProfile[]> {
    let params = new HttpParams();

    if (search?.trim()) {
      params = params.set('search', search.trim());
    }

    if (enabled !== undefined && enabled !== null) {
      params = params.set('enabled', String(enabled));
    }

    return this.http.get<UserProfile[]>(this.userApi, { params });
  }

  getUser(id: number): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.userApi}/${id}`);
  }

  listRoles(): Observable<RoleOption[]> {
    return this.http.get<RoleOption[]>(`${this.userApi}/roles`);
  }

  createUser(payload: UserUpsertRequest): Observable<UserProfile> {
    return this.http.post<UserProfile>(this.userApi, payload);
  }

  updateUser(id: number, payload: UserUpsertRequest): Observable<UserProfile> {
    return this.http.put<UserProfile>(`${this.userApi}/${id}`, payload);
  }

  updateUserStatus(id: number, enabled: boolean): Observable<UserProfile> {
    const params = new HttpParams().set('enabled', String(enabled));
    return this.http.patch<UserProfile>(`${this.userApi}/${id}/status`, {}, { params });
  }

  private unwrapCollection<T>(response: T[] | PaginatedResponse<T>): T[] {
    if (Array.isArray(response)) {
      return response;
    }

    return Array.isArray(response?.content) ? response.content : [];
  }
}
