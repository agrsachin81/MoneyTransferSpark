
package home.test.restapi.routing;

import spark.Route;

/**
 * interface is just created to mock the Spark so that the routing code be unit tested
 * @author sachin
 *
 * Type RestfulRouter, created on 24-Sep-2019 at 3:22:22 pm
 *
 */
public interface RestfulRouter {

	void post(String path, Route route);

	void get(String path, Route route);

	void put(String path, Route route);

	void delete(String path, Route route);

	void options(String path, Route route);

}