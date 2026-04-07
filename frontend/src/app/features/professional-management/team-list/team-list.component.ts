import { Component, OnInit } from '@angular/core';
import { ProfessionalManagementService } from '../professional-management.service';
import { TeamDto } from '../models/department.model';

@Component({
  selector: 'app-team-list',
  templateUrl: './team-list.component.html',
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
export class TeamListComponent implements OnInit {
  teams: TeamDto[] = [];
  filteredTeams: TeamDto[] = [];
  searchQuery = '';
  loading = false;
  error: string | null = null;
  selectedTeam: TeamDto | null = null;
  showForm = false;

  constructor(private service: ProfessionalManagementService) {}

  ngOnInit(): void {
    this.loadTeams();
  }

  loadTeams(): void {
    this.loading = true;
    this.error = null;
    this.service.listTeams().subscribe({
      next: (data) => {
        this.teams = data;
        this.filteredTeams = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar equipes: ' + err.message;
        this.loading = false;
      }
    });
  }

  search(): void {
    if (this.searchQuery.trim()) {
      this.loading = true;
      this.service.searchTeams(this.searchQuery).subscribe({
        next: (data) => {
          this.filteredTeams = data;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Erro ao buscar equipes: ' + err.message;
          this.loading = false;
        }
      });
    } else {
      this.filteredTeams = this.teams;
    }
  }

  selectTeam(team: TeamDto): void {
    this.selectedTeam = team;
  }

  openForm(): void {
    this.showForm = true;
    this.selectedTeam = null;
  }

  closeForm(): void {
    this.showForm = false;
  }

  onTeamSaved(team: TeamDto): void {
    this.loadTeams();
    this.closeForm();
  }

  deleteTeam(id: number | undefined): void {
    if (id && confirm('Tem certeza que deseja desativar esta equipe?')) {
      this.service.deactivateTeam(id).subscribe({
        next: () => {
          this.loadTeams();
          this.selectedTeam = null;
        },
        error: (err) => {
          this.error = 'Erro ao desativar equipe: ' + err.message;
        }
      });
    }
  }
}

