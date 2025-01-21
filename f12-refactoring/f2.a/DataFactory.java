package components;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import datamodel.Order.OrderItem;
import datamodel.Pricing.PricingCategory;
import datamodel.Pricing.TAXRate;


/**
 * Public <i>factory</i>-interface with <i>create()-</i>methods to instantiate objects
 * of classes of the {@link datamodel} package from validated parameters.
 * Objects are only created from valid parameters.
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public interface DataFactory {

    /**
     * <i>Factory</i> method to create an object of class {@link Customer} from
     * validated parameters. The <i>id</i> attribute is internally provided.
     * No object is created when arguments are not valid.
     * @param name single-String name parameter, invalid if null or empty,
     *          example: "Eric Meyer" or "Meyer, Eric"
     * @param contact contact parameter validated as an email address
     *          containing '@' or a phone number, invalid if null or empty
     * @return created {@link Customer} object with valid parameters or empty
     */
    Optional<Customer> createCustomer(String name, String contact);

    /**
     * <i>Factory</i> method to create an object of class {@link Article} from
     * validated arguments. The <i>id</i> attribute is internally provided.
     * No object is created when arguments are not valid.
     * @param description brief article description, e.g. "Tasse"
     * @param unitPrice price of one unit (in cent)
     * @param pricingCategory pricing table associated with this article
     * @param taxRate rate according to {@link TAXRate} ({@code TAXRate.Regular} is default)
     * @return {@link Article} object created from valid arguments or empty
     */
    Optional<Article> createArticle(
        String description,
        long unitPrice,
        PricingCategory pricingCategory,
        TAXRate... taxRate
    );

    /**
     * Create {@link OrderBuilder} object used to build {@link Order} objects.
     * @param pricingCategory pricing category used to build order
     * @param customerFetcher function to fetch {@link Customer} object from spec-String matching customer id, first or last name.
     * @param articleFetcher function to fetch {@link Article} object from spec-String matching article id or description.
     * @return {@link OrderBuilder} object used to build {@link Order} objects
     * @throws IllegalArgumentException thrown by {@link OrderBuilder} constructor if arguments are null
     */
    OrderBuilder createOrderBuilder(
        PricingCategory pricingCategory,
        Function<String, Optional<Customer>> customerFetcher,
        Function<String, Optional<Article>> articleFetcher
    );

    /**
     * Interface to build {@link Order} objects as a multi-step process using
     * the <i>builder</i> pattern.
     */
    interface OrderBuilder {

        /**
         * Interface to hold interim state of an partially built {@link Order}
         * object. BuildState is passed during build steps.
         */
        interface BuildState {

            /**
             * Create and add {@link OrderItem} to order as part of the final build step.
             * @param unitsOrdered units of an article ordered
             * @param articleSpec specification of an article to look up by articleFetcher
             * @return chainable self-reference
             */
            public BuildState item(long unitsOrdered, String articleSpec);
        }

        /**
         * Method to build {@link Order} object.
         * @param customerSpec specification matching customer id, first or last name
         * @param buildState call-out to add {@link OrderItem} to order
         * @return fully built {@link Order} object or empty Optional
         */
        Optional<Order> buildOrder(String customerSpec, Consumer<BuildState> buildState);
    }
}