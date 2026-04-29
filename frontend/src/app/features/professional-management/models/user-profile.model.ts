export interface UserProfile {
  id: number;
  username: string;
  displayName: string;
  email: string;
  enabled: boolean;
  createdAt: string;
  roles: string[];
  departmentIds: number[];
  departmentNames: string[];
}

export interface RoleOption {
  name: string;
  description: string;
}

export interface UserUpsertRequest {
  username: string;
  email: string;
  password?: string;
  enabled: boolean;
  roles: string[];
  departmentIds: number[];
}
