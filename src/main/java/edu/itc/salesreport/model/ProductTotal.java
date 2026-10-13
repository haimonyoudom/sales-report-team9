
package edu.itc.salesreport.model;

import java.math.BigDecimal;

public record ProductTotal(
        String sku,
        String name,
        BigDecimal revenue,
        long quantity) {
}
