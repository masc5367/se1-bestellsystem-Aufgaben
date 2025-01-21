package components;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * Public interface of component to validate parameters for creating objects
 * of {@link datamodel} classes. Interface is primarily used by {@link DataFactory}.
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface Validator {

    /**
     * Validate order creation date against bounds {@code lowerOrderCreationDate}
     * ({@code "Jan 01, 2020 00:00"}) and {@code upperOrderCreationDate}
     * ({@code "Dec 31, 2099 23:59"}).
     * @param date date to validate
     * @return validated date or empty result
     */
    Optional<LocalDateTime> validateOrderCreationDate(LocalDateTime date);

    /**
     * Validate contact for acceptable email address or phone number and
     * return contact or empty result.
     * <br>
     * Rules for validating a <i>email</i> addresses and <i>phone</i>
     * numbers are defined by regular expressions:
     * <ul>
     * <li> <i>email address:</i> {@code "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z0-9_]+$"}.
     * <li> <i>phone number:</i> {@code "^(phone:|fax:|\\+[0-9]+){0,1}\\s*[\\s0-9()][\\s0-9()-]*"}.
     * <li> leading and trailing white spaces {@code [\s]}, commata {@code [,;]}
     *      and quotes {@code ["']} are trimmed from contacts before validation.
     * </ul>
     * @param contact contact to validate
     * @return possibly modified (e.g. dequoted, trimmed) valid contact or empty result
     */
    Optional<String> validateContact(String contact);

    /**
     * Validate name and return name or empty result. A valid name must
     * start with a letter, followed by a combination of letters, "-",
     * "." or white spaces. Valid names are: "E", "E.", "Eric",
     * "Ulla-Nadine", "Eric Meyer", "von-Blumenfeld".
     * Names do not include numbers or other special characters.
     * <br>
     * Rules for validating a <i>name</i> are defined by a regular expression:
     * <ul>
     * <li> {@code "^[A-Za-z][A-Za-z-\\s.]*$"}.
     * <li> leading and trailing white spaces {@code [\s]}, commata {@code [,;]} and
     *      quotes {@code ["']} are trimmed from names before validation, e.g.
     *      {@code "  'Schulz-Müller, Tim Anton'  "}.
     * </ul>
     * <pre>
     * Examples:
     * +------------------------------------+---------------------------------------+
     * |name to validate                    |valid, possibly modified name          |
     * +------------------------------------+---------------------------------------+
     * |"Eric"                              |"Eric"                                 |
     * |"Ulla-Nadine"                       |"Ulla-Nadine"                          |
     * |"E", "E.", "von-A"                  |"E", "E.", "von-A"                     |
     * +------------------------------------+---------------------------------------+
     *
     * Trim leading, trailing white spaces and quotes:
     * +------------------------------------+---------------------------------------+
     * |"  Anne  "   (lead/trailing spaces) |"Anne"                                 |
     * |"  'Meyer'  "   (quotes)            |"Meyer"                                |
     * +------------------------------------+---------------------------------------+
     * </pre>
     * @param name name to validate
     * @param acceptEmptyName accept empty ("") name, e.g. as first name
     * @return valid, possibly modified (e.g. dequoted, trimmed) name or empty result
     */
    Optional<String> validateName(String name, boolean acceptEmptyName);

    /**
     * Record of first and last name parts of a name.
     * @param first first name parts
     * @param last last name parts
     * @hidden exclude from documentation
     */
    record NameParts(
        String first,
        String last
    ) { }

    /**
     * Split single-String name into first and last name parts and
     * validate parts, e.g. "Meyer, Eric" is split into first: "Eric"
     * and last name: "Meyer".
     * <br>
     * Rules of splitting a single-String name into last- and first name parts:
     * <ul>
     * <li> if a name contains no seperators (comma or semicolon {@code [,;]}), the trailing
     *      consecutive part is the last name, all prior parts are first name parts, e.g.
     *      {@code "Tim Anton Schulz-Müller"}, splits into <i>first name:</i>
     *      {@code "Tim Anton"} and <i>last name:</i> {@code "Schulz-Müller"}.
     * <li> names with seperators (comma or semicolon {@code [,;]}) split into a last name
     *      part before the seperator and a first name part after the seperator, e.g.
     *      {@code "Schulz-Müller, Tim Anton"} splits into <i>first name:</i>
     *      {@code "Tim Anton"} and <i>last name:</i> {@code "Schulz-Müller"}.
     * <li> leading and trailing white spaces {@code [\s]}, commata {@code [,;]} and quotes
     *      {@code ["']} are trimmed from names before validation, e.g.
     *      {@code "  'Schulz-Müller, Tim Anton'  "}.
     * <li> interim white spaces between name parts are removed, e.g.
     *      {@code "Schulz-Müller, <white-spaces> Tim <white-spaces> Anton <white-spaces> "}.
     * </ul>
     * <pre>
     * Examples:
     * +------------------------------------+-------------------+-------------------+
     * |Single-String name                  |first name parts   |last name parts    |
     * +------------------------------------+-------------------+-------------------+
     * |"Eric Meyer"                        |"Eric"             |"Meyer"            |
     * |"Meyer, Anne"                       |"Anne"             |"Meyer"            |
     * |"Meyer; Anne"                       |"Anne"             |"Meyer"            |
     * |"Tim Schulz‐Mueller"                |"Tim"              |"Schulz‐Mueller"   |
     * |"Nadine Ulla Blumenfeld"            |"Nadine Ulla"      |"Blumenfeld"       |
     * |"Nadine‐Ulla Blumenfeld"            |"Nadine‐Ulla"      |"Blumenfeld"       |
     * |"Khaled Mohamed Abdelalim"          |"Khaled Mohamed"   |"Abdelalim"        |
     * +------------------------------------+-------------------+-------------------+
     *
     * Trim leading, trailing and interim white spaces and quotes:
     * +------------------------------------+-------------------+-------------------+
     * |" 'Eric Meyer'  "                   |"Eric"             |"Meyer"            |
     * |"Nadine     Ulla     Blumenfeld"    |"Nadine Ulla"      |"Blumenfeld"       |
     * +------------------------------------+-------------------+-------------------+
     * </pre>
     * @param name single-String name to split into first- and last name parts
     * @return record with valid, possibly modified (e.g. dequoted, trimmed) first and last name parts or empty result
     */
    Optional<NameParts> validateSplitName(String name);
}