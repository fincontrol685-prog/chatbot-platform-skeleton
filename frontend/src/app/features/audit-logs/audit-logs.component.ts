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
  standalone: true,
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

  filterAction = '';
  filterEntityType = '';
  filterStatus = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Placeholder for loading audit logs
    this.loadAuditLogs();
  }

  loadAuditLogs(): void {
    // Call to AuditService when available
    console.log('Loading audit logs with filters:', {
      action: this.filterAction,
      entityType: this.filterEntityType,
      status: this.filterStatus
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadAuditLogs();
  }

  viewDetails(log: AuditLog): void {
    // Show detailed change information
    const oldValue = log.oldValue ? JSON.parse(log.oldValue) : null;
    const newValue = log.newValue ? JSON.parse(log.newValue) : null;
    console.log('Old Value:', oldValue);
    console.log('New Value:', newValue);
    // Show modal or side panel with details
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
}

