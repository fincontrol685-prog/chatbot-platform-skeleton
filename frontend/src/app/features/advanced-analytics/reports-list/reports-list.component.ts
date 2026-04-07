import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdvancedAnalyticsService } from '../advanced-analytics.service';
import { CustomReportDto, ReportDefinition } from '../models/analytics.model';

@Component({
  selector: 'app-reports-list',
  templateUrl: './reports-list.component.html',
  styles: [`
    .container {
      padding: 20px;
    }

    h2 {
      margin-bottom: 20px;
      color: #1976d2;
    }

    .toolbar {
      display: flex;
      gap: 10px;
      margin-bottom: 20px;
      align-items: center;
      flex-wrap: wrap;
    }

    .search-field {
      flex: 1;
      min-width: 250px;
      max-width: 400px;
    }

    .loading {
      display: flex;
      justify-content: center;
      padding: 40px 20px;
    }

    .reports-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 20px;
      margin-bottom: 20px;
    }

    .report-card {
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s;
    }

    .report-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    mat-card-header {
      display: flex;
      gap: 15px;
    }

    [mat-card-avatar] {
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #1976d2;
      color: white;
      border-radius: 4px;
    }

    [mat-card-avatar] mat-icon {
      font-size: 20px;
      width: 20px;
      height: 20px;
    }

    .report-meta {
      display: flex;
      gap: 10px;
      margin-top: 15px;
    }

    .public-badge,
    .private-badge {
      display: inline-flex;
      align-items: center;
      gap: 5px;
      padding: 4px 8px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
    }

    .public-badge {
      background-color: #e8f5e9;
      color: #2e7d32;
    }

    .private-badge {
      background-color: #f3e5f5;
      color: #6a1b9a;
    }

    .public-badge mat-icon,
    .private-badge mat-icon {
      font-size: 14px;
      width: 14px;
      height: 14px;
    }

    .empty-state {
      text-align: center;
      padding: 60px 20px;
      color: #666;
    }

    .empty-state mat-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      margin-bottom: 15px;
      opacity: 0.2;
    }

    .detail-card,
    .form-card {
      margin-top: 20px;
      width: 90%;
      max-width: 600px;
    }

    p {
      margin: 10px 0;
      line-height: 1.6;
    }

    strong {
      color: #333;
    }
  `]
})
export class ReportsListComponent implements OnInit {
  reports: CustomReportDto[] = [];
  myReports: CustomReportDto[] = [];
  filteredReports: CustomReportDto[] = [];
  searchQuery = '';
  loading = false;
  error: string | null = null;
  selectedReport: CustomReportDto | null = null;
  showForm = false;
  showAllReports = false;

  constructor(
    private fb: FormBuilder,
    private service: AdvancedAnalyticsService
  ) {}

  ngOnInit(): void {
    this.loadMyReports();
  }

  loadMyReports(): void {
    this.loading = true;
    this.error = null;
    this.service.getMyReports().subscribe({
      next: (data) => {
        this.myReports = data;
        this.filteredReports = data;
        this.showAllReports = false;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar relatórios: ' + err.message;
        this.loading = false;
      }
    });
  }

  loadAccessibleReports(): void {
    this.loading = true;
    this.error = null;
    this.service.listAccessibleReports().subscribe({
      next: (data) => {
        this.reports = data;
        this.filteredReports = data;
        this.showAllReports = true;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar relatórios: ' + err.message;
        this.loading = false;
      }
    });
  }

  search(): void {
    if (this.searchQuery.trim()) {
      this.filteredReports = (this.showAllReports ? this.reports : this.myReports).filter(r =>
        r.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        (r.description && r.description.toLowerCase().includes(this.searchQuery.toLowerCase()))
      );
    } else {
      this.filteredReports = this.showAllReports ? this.reports : this.myReports;
    }
  }

  selectReport(report: CustomReportDto): void {
    this.selectedReport = report;
  }

  openForm(): void {
    this.showForm = true;
    this.selectedReport = null;
  }

  closeForm(): void {
    this.showForm = false;
  }

  onReportSaved(report: CustomReportDto): void {
    this.loadMyReports();
    this.closeForm();
  }

  deleteReport(id: number | undefined): void {
    if (id && confirm('Tem certeza que deseja deletar este relatório?')) {
      this.service.deleteReport(id).subscribe({
        next: () => {
          this.loadMyReports();
          this.selectedReport = null;
        },
        error: (err) => {
          this.error = 'Erro ao deletar relatório: ' + err.message;
        }
      });
    }
  }

  toggleViewMode(): void {
    if (this.showAllReports) {
      this.loadMyReports();
    } else {
      this.loadAccessibleReports();
    }
  }
}

