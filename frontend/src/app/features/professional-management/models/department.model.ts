export interface DepartmentDto {
  id?: number;
  name: string;
  code: string;
  location?: string;
  description?: string;
  managerId?: number;
  parentDepartmentId?: number;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface TeamDto {
  id?: number;
  name: string;
  code: string;
  description?: string;
  departmentId: number;
  teamLeadId?: number;
  maxConversationsPerUser?: number;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface TeamMemberDto {
  id?: number;
  teamId: number;
  userId: number;
  role: 'TEAM_LEAD' | 'MEMBER' | 'MODERATOR';
  joinedAt?: string;
}

export interface DepartmentHierarchyDto extends DepartmentDto {
  subdepartments?: DepartmentHierarchyDto[];
  teams?: TeamDto[];
}

