package components;

import datamodel.Customer;
import datamodel.Pricing;
import datamodel.Pricing.Currency;


/**
 * Public interface of the <i>formatter</i>-component that provides text formatting
 * methods including a {@link TableFormatter} that produces text in a table format.
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface Formatter {

    /**
     * Format Customer name according to a format (0 is default):
     * <pre>
     * style: 0: "Meyer, Eric"  10: "MEYER, ERIC"
     *        1: "Eric Meyer"   11: "ERIC MEYER"
     *        2: "Meyer, E."    12: "MEYER, E."
     *        3: "E. Meyer"     13: "E. MEYER"
     *        4: "Meyer"        14: "MEYER"
     *        5: "Eric"         15: "ERIC"
     * </pre>
     * @param customer Customer object
     * @param style name formatting style
     * @return Customer name formatted according to the selcted style
     * @throws IllegalArgumentException with null arguments
     */
    public String fmtCustomerName(Customer customer, int... style);

    /**
     * Format Customer contacts according to a format (0 is default):
     * <pre>
     * style: 0: first contact: "anne24@yahoo.de"
     *        1: first contact with extension indicator: "anne24@yahoo.de, (+2 contacts)"
     *        2: all contacts as list: "anne24@yahoo.de, (030) 3481-23352, fax: (030)23451356"
     * </pre>
     * @param customer Customer object
     * @param style name formatting style
     * @return Customer contact information formatted according to the selcted style
     */
    public String fmtCustomerContacts(Customer customer, int... style);


    /**
     * Format long value to price according to a format (0 is default):
     * <pre>
     * Example: long value: 499
     * style: 0: "4.99"
     *        1: "4.99 EUR"     3: "4.99 €"
     *        2: "4.99EUR"      4: "4.99€"
     * </pre>
     * @param price long value as price
     * @param currency {@link Currency} to obtain currency three-letter code or Unicode
     * @param style price formatting style
     * @return price formatted according to selcted style
     */
    public String fmtPrice(long price, Pricing.Currency currency, int... style);

    /**
     * Format long value to a decimal String with specified digit formatting:
     * <pre>
     *      {      "%,d", 1L },     // no decimal digits:  16,000Y
     *      { "%,d.%01d", 10L },
     *      { "%,d.%02d", 100L },   // double-digit price: 169.99E
     *      { "%,d.%03d", 1000L },  // triple-digit unit:  16.999-
     * </pre>
     * @param value value to format to String in decimal format
     * @param decimalDigits number of digits
     * @param unit appended unit as String
     * @return decimal value formatted according to specified digit formatting
     */
    public String fmtDecimal(long value, int decimalDigits, String... unit);


    /**
     * Public interface of the <i>TableFormatter</i>-component that produces text in
     * form of a table defined by columns of specified <i>width</i> and <i>alignment</i>.
     * {@code String.format(fmt)} specifications: <i>fmt</i> format cells in a row
     * according to <i>width</i> and <i>alignment</i> specifications.
     */
    public interface TableFormatter {

        /**
         * Add row to table. Each cell is formatted according to the column fmt specifier.
         * @param cells variable array of cells
         * @return chainable self-reference
         */
        public TableFormatter row(String... cells);

        /**
         * Add line comprised of segments for each column to the table.
         * Segments are drawn based on segment spefifiers with:
         * <pre>
         * seg: null    - empty or blank segment
         *      ""      - segment filled with default character: "-"
         *      "="     - segment is filled with provided character.
         * </pre>
         * @param segments variable array of segment specifiers
         * @return chainable self-reference
         */
        public TableFormatter line(String... segments);

        /**
         * Getter to collected table content.
         * @return table content
         */
        public StringBuilder get();
    }

    /**
     * Create a {@link TableFormatter} with {@code String.format(fmt)} specifiers
     * for each column.
     * <br>
     * Examples of {@code columnSpec}-specifiers:
     * <pre>
     *  - "|%-10s|",        -- column with left and right border of width 10 (String)
     *  - " %-28s",         -- column with space to the left, no border of width 29 (String)
     *  - "| %6s", " %9s|"  -- two columns of widths 6 and 9 with borders left and right
     * </pre>
     * @param columnSpecs String.format(fmt) specifiers for each column
     */
    public TableFormatter createTableFormatter(String... columnSpecs);

}