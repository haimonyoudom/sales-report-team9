
package edu.itc.salesreport.render;

import edu.itc.salesreport.model.MonthlyReport;

public sealed interface ReportRenderer
        permits TextReportRenderer, HtmlReportRenderer, CsvReportRenderer {

    // File extension without the dot.
    String extension();

    // Convert the report to bytes.
    byte[] render(MonthlyReport report);

    // Select the renderer based on the requested extension.
    static ReportRenderer forExtension(String ext) {
        return switch (ext) {
            case "txt" -> new TextReportRenderer();
            case "html" -> new HtmlReportRenderer();
            case "csv" -> new CsvReportRenderer();
            default -> throw new IllegalArgumentException(
                    "no renderer for ." + ext);
        };
    }
}
