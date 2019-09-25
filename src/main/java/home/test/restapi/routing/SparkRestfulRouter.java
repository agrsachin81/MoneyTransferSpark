
package home.test.restapi.routing;
import spark.Route;
import spark.Spark;

/**
 * just a wrapper to instrument Spark static imports
 * @author sachin
 *
 * Type SparkRestfulRouter, created on 22-Sep-2019 at 5:48:09 pm
 *
 */
public class SparkRestfulRouter implements RestfulRouter  {
	
	public SparkRestfulRouter() {
	}
	
	@Override
	public void post(String path, Route route){
		Spark.post(path, route);
	}
	
	@Override
	public void get(String path, Route route){
		Spark.get(path, route);
	}
	
	
	@Override
	public void put(String path, Route route){
		Spark.put(path, route);
	}
	
	@Override
	public void delete(String path, Route route){
		Spark.delete(path, route);
	}
	
	@Override
	public void options(String path, Route route){
		Spark.options(path, route);
	}
}
