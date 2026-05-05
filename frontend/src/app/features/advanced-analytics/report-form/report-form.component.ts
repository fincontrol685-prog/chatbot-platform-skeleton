import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdvancedAnalyticsService } from '../advanced-analytics.service';
import { CustomReportDto } from '../models/analytics.model';

@Component({
    selector: 'app-report-form',
    templateUrl: './report-form.component.html',
    styles: [`
    .full-width {
      width: 100%;
      margin-bottom: 15px;
    }

    .form-section {
      margin-top: 25px;
      padding: 15px;
      background-color: #f9f9f9;
      border-radius: 4px;
    }

    .form-section h3 {
      margin-top: 0;
      margin-bottom: 15px;
      color: #333;
      font-size: 16px;
      font-weight: 500;
    }

    .metrics-list {
      max-height: 300px;
      overflow-y: auto;
      border: 1px solid #ddd;
      border-radius: 4px;
      background-color: #fff;
    }

    .form-actions {
      display: flex;
      gap: 10px;
      margin-top: 20px;
      padding-top: 15px;
      border-top: 1px solid #ddd;
    }

    mat-checkbox {
      margin: 15px 0;
    }

    form {
      padding: 10px 0;
    }

    mat-spinner {
      margin-right: 8px;
    }
  `],
    standalone: false
})
export class ReportFormComponent implements OnInit {
  @Input() report: CustomReportDto | null = null;
  @Output() saved = new EventEmitter<CustomReportDto>();
  @Output() cancelled = new EventEmitter<void>();

  form: FormGroup;
  loading = false;
  error: string | null = null;

  reportTypes = ['TABLE', 'CHART', 'DASHBOARD'];
  chartTypes = ['LINE', 'BAR', 'PIE', 'AREA'];
  groupByOptions = ['bot', 'team', 'department', 'date'];
  metricOptions = [
    'CONVERSATION_COUNT',
    'RESPONSE_TIME',
    'SATISFACTION_SCORE',
    'RESOLUTION_TIME',
    'BOT_INTERACTIONS',
    'USER_RETENTION'
  ];

  constructor(
    private fb: FormBuilder,
    private service: AdvancedAnalyticsService
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      isPublic: [false],
      reportDefinition: this.fb.group({
        title: ['', Validators.required],
        type: ['TABLE', Validators.required],
        chartType: ['LINE'],
        groupBy: ['date']
      }),
      metricTypes: [[], Validators.required]
    });
  }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    if (this.report) {
      this.form.patchValue({
        name: this.report.name,
        description: this.report.description,
        isPublic: this.report.isPublic,
        reportDefinition: this.report.reportDefinition,
        metricTypes: this.report.metricTypes
      });
    }
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }

    this.loading = true;
    this.error = null;

    const data = this.form.value;
    const request = this.report
      ? this.service.updateReport(this.report.id!, data)
      : this.service.createReport(data);

    request.subscribe({
      next: (result) => {
        this.loading = false;
        this.saved.emit(result);
      },
      error: (err) => {
        this.error = 'Erro ao salvar relatório: ' + err.message;
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.cancelled.emit();
  }

  onTypeChange(): void {
    const type = this.form.get('reportDefinition.type')?.value;
    if (type === 'TABLE') {
      this.form.get('reportDefinition.chartType')?.disable();
    } else {
      this.form.get('reportDefinition.chartType')?.enable();
    }
  }
}

