import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { AuditLogService } from './audit-log.service';
import { getApiErrorMessage } from '../../core/api-error.util';

export interface AuditLog {
  id: number;
  username: string;
  action: string;
  entityType: string;
  entityId: number;
  oldValue: string;
  newValue: string;
  ipAddress: string;
  status: string;
  createdAt: string;
}

@Component({
    selector: 'app-audit-logs',
    imports: [
        CommonModule,
        MatTableModule,
        MatPaginatorModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatChipsModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        FormsModule,
        RouterModule
    ],
    templateUrl: './audit-logs.component.html',
    styleUrls: ['./audit-logs.component.css']
})
export class AuditLogsComponent implements OnInit {
  displayedColumns: string[] = ['id', 'username', 'action', 'entityType', 'status', 'ipAddress', 'createdAt', 'actions'];
  auditLogs: AuditLog[] = [];
  totalElements = 0;
  pageSize = 10;
  currentPage = 0;
  loading = false;
  error = '';

  filterAction = '';
  filterEntityType = '';
  filterStatus = '';

  constructor(
    private route: ActivatedRoute,
    private auditLogService: AuditLogService
  ) {}

  ngOnInit(): void {
    // Placeholder for loading audit logs
    this.loadAuditLogs();
  }

  loadAuditLogs(): void {
    this.loading = true;
    this.error = '';

    this.auditLogService.list({
      action: this.filterAction,
      entityType: this.filterEntityType,
      status: this.filterStatus,
      page: this.currentPage,
      size: this.pageSize
    }).subscribe({
      next: (response) => {
        this.auditLogs = response.content ?? [];
        this.totalElements = response.totalElements ?? 0;
        this.loading = false;
      },
      error: (err) => {
        this.auditLogs = [];
        this.totalElements = 0;
        this.error = getApiErrorMessage(err, 'Nao foi possivel carregar os logs de auditoria.');
        this.loading = false;
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadAuditLogs();
  }

  viewDetails(log: AuditLog): void {
    const oldValue = this.parseJsonSafely(log.oldValue);
    const newValue = this.parseJsonSafely(log.newValue);
    console.log('Old Value:', oldValue);
    console.log('New Value:', newValue);
  }

  getActionColor(action: string): string {
    const colors: { [key: string]: string } = {
      'CREATE': 'accent',
      'UPDATE': 'warn',
      'DELETE': 'primary',
      'VIEW': 'primary'
    };
    return colors[action] || 'primary';
  }

  getStatusColor(status: string): string {
    return status === 'SUCCESS' ? 'accent' : 'warn';
  }

  private parseJsonSafely(value: string): unknown {
    if (!value) {
      return null;
    }

    try {
      return JSON.parse(value);
    } catch {
      return value;
    }
  }
}
