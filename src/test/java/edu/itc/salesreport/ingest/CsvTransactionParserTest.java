
package edu.itc.salesreport.ingest;

import edu.itc.salesreport.model.PaymentMethod;
import edu.itc.salesreport.model.SaleTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CsvTransactionParserTest {

    private final TransactionParser parser =
            new CsvTransactionParser();

    @Test
    void parsesAValidRow() throws InvalidRowException {
        SaleTransaction t = parser.parse(
                "PNH,2026-09-01,PNH-000123,"
                + "SKU-1001,Jasmine Rice 5kg,Grocery,"
                + "2,6.50,0.00,KHQR");

        assertEquals("PNH", t.branch());
        assertEquals(LocalDate.parse("2026-09-01"), t.date());
        assertEquals("Jasmine Rice 5kg", t.productName());
        assertEquals(2, t.quantity());
        assertEquals(new BigDecimal("6.50"), t.unitPrice());
        assertEquals(PaymentMethod.KHQR, t.paymentMethod());
        assertEquals(new BigDecimal("13.00"), t.revenue());
    }

    @Test
    void parsesDiscountedRow() throws InvalidRowException {
        SaleTransaction t = parser.parse(
                "PNH,2026-09-01,PNH-000124,"
                + "SKU-1001,Jasmine Rice 5kg,Grocery,"
                + "2,6.50,1.00,KHQR");

        assertEquals(new BigDecimal("12.00"), t.revenue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "PNH,2026-09-01,PNH-1,SKU-1,Rice,Grocery,2,6.50,0.00",
        "XXX,2026-09-01,PNH-1,SKU-1,Rice,Grocery,2,6.50,0.00,CASH",
        "PNH,2026-09-31,PNH-1,SKU-1,Rice,Grocery,2,6.50,0.00,CASH",
        "PNH,2026-09-01,PNH-1,SKU-1,Rice,Unknown,2,6.50,0.00,CASH",
        "PNH,2026-09-01,PNH-1,SKU-1,Rice,Grocery,abc,6.50,0.00,CASH",
        "PNH,2026-09-01,PNH-1,SKU-1,Rice,Grocery,0,6.50,0.00,CASH",
        "PNH,2026-09-01,PNH-1,SKU-1,Rice,Grocery,2,6.50,-1.00,CASH",
        "PNH,2026-09-01,PNH-1,SKU-1,Rice,Grocery,2,6.50,20.00,CASH",
        "PNH,2026-09-01,PNH-1,SKU-1,Rice,Grocery,2,6.50,0.00,UNKNOWN"
    })
    void rejectsInvalidRows(String line) {
        assertThrows(
                InvalidRowException.class,
                () -> parser.parse(line));
    }
}
