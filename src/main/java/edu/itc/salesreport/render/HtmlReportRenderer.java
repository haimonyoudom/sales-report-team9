
package edu.itc.salesreport.render;

import edu.itc.salesreport.model.BranchSummary;
import edu.itc.salesreport.model.MonthlyReport;
import java.nio.charset.StandardCharsets;

public final class HtmlReportRenderer implements ReportRenderer {

    @Override
    public String extension() {
        return "html";
    }

    @Override
    public byte[] render(MonthlyReport report) {
        var sb = new StringBuilder();

        sb.append("<!DOCTYPE html>\n")
          .append("<html>\n<head>\n")
          .append("<meta charset=\"UTF-8\">\n")
          .append("<title>Angkor Mart Sales</title>\n")
          .append("</head>\n<body>\n")
          .append("<h1>Angkor Mart monthly sales ")
          .append(escape(report.month().toString()))
          .append("</h1>\n")
          .append("<table>\n")
          .append("<thead><tr>")
          .append("<th>Branch</th>")
          .append("<th>Revenue</th>")
          .append("<th>Receipts</th>")
          .append("<th>Average basket</th>")
          .append("</tr></thead>\n<tbody>\n");

        for (BranchSummary branch : report.branches()) {
            appendRow(sb, branch);
        }

        appendRow(sb, report.chain());

        sb.append("</tbody>\n</table>\n")
          .append("</body>\n</html>\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static void appendRow(
            StringBuilder sb, BranchSummary branch) {

        sb.append("<tr><td>")
          .append(escape(branch.branch()))
          .append("</td><td>")
          .append(escape(branch.revenue().toString()))
          .append("</td><td>")
          .append(branch.receipts())
          .append("</td><td>")
          .append(escape(branch.averageBasket().toString()))
          .append("</td></tr>\n");
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
    }
}
