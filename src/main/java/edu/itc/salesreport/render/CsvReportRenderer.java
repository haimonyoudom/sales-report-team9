
package edu.itc.salesreport.render;

import edu.itc.salesreport.model.BranchSummary;
import edu.itc.salesreport.model.MonthlyReport;
import java.nio.charset.StandardCharsets;

public final class CsvReportRenderer implements ReportRenderer {

    @Override
    public String extension() {
        return "csv";
    }

    @Override
    public byte[] render(MonthlyReport report) {
        var sb = new StringBuilder();

        sb.append("month,branch,revenue,discounts,receipts,average_basket\n");

        for (BranchSummary branch : report.branches()) {
            appendRow(sb, report, branch);
        }

        appendRow(sb, report, report.chain());

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static void appendRow(
            StringBuilder sb,
            MonthlyReport report,
            BranchSummary branch) {

        sb.append(report.month()).append(',')
          .append(branch.branch()).append(',')
          .append(branch.revenue()).append(',')
          .append(branch.discounts()).append(',')
          .append(branch.receipts()).append(',')
          .append(branch.averageBasket()).append('\n');
    }
}
