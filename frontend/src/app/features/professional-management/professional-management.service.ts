import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DepartmentDto, TeamDto, TeamMemberDto, DepartmentHierarchyDto } from './models/department.model';

@Injectable({ providedIn: 'root' })
export class ProfessionalManagementService {
  private apiBase = environment.apiUrl || '';
  private deptApi = `${this.apiBase}/api/departments`;
  private teamApi = `${this.apiBase}/api/teams`;

  constructor(private http: HttpClient) {}

  // ===== DEPARTMENTS =====
  listDepartments(): Observable<DepartmentDto[]> {
    return this.http.get<DepartmentDto[]>(this.deptApi);
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
    return this.http.get<DepartmentDto[]>(`${this.deptApi}/search`, { params });
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
    return this.http.get<TeamDto[]>(this.teamApi);
  }

  getTeam(id: number): Observable<TeamDto> {
    return this.http.get<TeamDto>(`${this.teamApi}/${id}`);
  }

  getTeamsByDepartment(departmentId: number): Observable<TeamDto[]> {
    return this.http.get<TeamDto[]>(`${this.teamApi}/department/${departmentId}`);
  }

  searchTeams(query: string): Observable<TeamDto[]> {
    const params = new HttpParams().set('q', query);
    return this.http.get<TeamDto[]>(`${this.teamApi}/search`, { params });
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
}

