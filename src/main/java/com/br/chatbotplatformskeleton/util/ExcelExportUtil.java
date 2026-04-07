package com.br.chatbotplatformskeleton.util;

import com.br.chatbotplatformskeleton.dto.AnalyticsMetricDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Utilitário para exportação de relatórios em Excel
 */
public class ExcelExportUtil {

    public static byte[] exportMetricsToExcel(List<AnalyticsMetricDto> metrics, String reportName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(reportName != null ? reportName : "Analytics");

        // Criar header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Metric Type", "Value", "Bot ID", "Department ID", "Team ID", "User ID", "Period Date", "Hour", "Dimension", "Recorded At"};

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Adicionar dados
        int rowNum = 1;
        for (AnalyticsMetricDto metric : metrics) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(metric.getMetricType() != null ? metric.getMetricType() : "");
            row.createCell(1).setCellValue(metric.getMetricValue() != null ? metric.getMetricValue() : 0.0);
            row.createCell(2).setCellValue(metric.getBotId() != null ? metric.getBotId().toString() : "");
            row.createCell(3).setCellValue(metric.getDepartmentId() != null ? metric.getDepartmentId().toString() : "");
            row.createCell(4).setCellValue(metric.getTeamId() != null ? metric.getTeamId().toString() : "");
            row.createCell(5).setCellValue(metric.getUserId() != null ? metric.getUserId().toString() : "");
            row.createCell(6).setCellValue(metric.getPeriodDate() != null ? metric.getPeriodDate().toString() : "");
            row.createCell(7).setCellValue(metric.getPeriodHour() != null ? metric.getPeriodHour().toString() : "");
            row.createCell(8).setCellValue(metric.getDimensionValue() != null ? metric.getDimensionValue() : "");
            row.createCell(9).setCellValue(metric.getRecordedAt() != null ? metric.getRecordedAt().toString() : "");
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

    public static byte[] exportMetricsToCSV(List<AnalyticsMetricDto> metrics) {
        StringBuilder csv = new StringBuilder();

        // Header
        csv.append("Metric Type,Value,Bot ID,Department ID,Team ID,User ID,Period Date,Hour,Dimension,Recorded At\n");

        // Data
        for (AnalyticsMetricDto metric : metrics) {
            csv.append(escapeCSV(metric.getMetricType())).append(",");
            csv.append(metric.getMetricValue()).append(",");
            csv.append(metric.getBotId() != null ? metric.getBotId() : "").append(",");
            csv.append(metric.getDepartmentId() != null ? metric.getDepartmentId() : "").append(",");
            csv.append(metric.getTeamId() != null ? metric.getTeamId() : "").append(",");
            csv.append(metric.getUserId() != null ? metric.getUserId() : "").append(",");
            csv.append(metric.getPeriodDate() != null ? metric.getPeriodDate() : "").append(",");
            csv.append(metric.getPeriodHour() != null ? metric.getPeriodHour() : "").append(",");
            csv.append(escapeCSV(metric.getDimensionValue())).append(",");
            csv.append(metric.getRecordedAt() != null ? metric.getRecordedAt() : "").append("\n");
        }

        return csv.toString().getBytes();
    }

    private static String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

