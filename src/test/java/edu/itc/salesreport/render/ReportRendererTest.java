
package edu.itc.salesreport.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;

class ReportRendererTest {

    private static Stream<Arguments> renderers() {
        return Stream.of(
                Arguments.of(new TextReportRenderer(), "txt"),
                Arguments.of(new HtmlReportRenderer(), "html"),
                Arguments.of(new CsvReportRenderer(), "csv"));
    }

    @ParameterizedTest
    @MethodSource("renderers")
    void everyRendererIncludesBranchesAndChainTotal(
            ReportRenderer renderer, String extension) {

        var report = TestReports.september();

        var output = new String(
                renderer.render(report), StandardCharsets.UTF_8);

        assertEquals(extension, renderer.extension());

        assertTrue(output.contains("PNH"));
        assertTrue(output.contains("REP"));
        assertTrue(output.contains("BTB"));
        assertTrue(output.contains("ALL"));
        assertTrue(output.contains("2300.50"));
    }

    @Test
    void csvContainsExactRepRow() {
        var report = TestReports.september();

        var output = new String(
                new CsvReportRenderer().render(report),
                StandardCharsets.UTF_8);

        assertTrue(output.contains(
                "2026-09,REP,800.50,0.00,50,16.01"));
    }

    @Test
    void unsupportedExtensionThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ReportRenderer.forExtension("pdf"));
    }

    @Test
    void rendererSwitchIsExhaustive() {
        assertEquals("txt", formatName(new TextReportRenderer()));
        assertEquals("html", formatName(new HtmlReportRenderer()));
        assertEquals("csv", formatName(new CsvReportRenderer()));
    }

    private static String formatName(ReportRenderer renderer) {
        return switch (renderer) {
            case TextReportRenderer r -> r.extension();
            case HtmlReportRenderer r -> r.extension();
            case CsvReportRenderer r -> r.extension();
        };
    }
}
