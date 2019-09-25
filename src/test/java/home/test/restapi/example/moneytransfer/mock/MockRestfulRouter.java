

package home.test.restapi.example.moneytransfer.mock;

import java.util.HashMap;
import java.util.Map;

import home.test.restapi.routing.RestfulRouter;
import spark.Route;
import spark.route.HttpMethod;

public class MockRestfulRouter implements RestfulRouter{

	Map<String, Route> routes = new HashMap<>();
	
	@Override
	public void post(String path, Route route) {
		routes.put(HttpMethod.post+"_"+path, route);
	}

	@Override
	public void get(String path, Route route) {
		routes.put(HttpMethod.get+"_"+path, route);
	}

	@Override
	public void put(String path, Route route) {
		routes.put(HttpMethod.put+"_"+path, route);
	}

	@Override
	public void delete(String path, Route route) {
		routes.put(HttpMethod.delete+"_"+path, route);
	}

	@Override
	public void options(String path, Route route) {
		routes.put(HttpMethod.options+"_"+path, route);
	}
	
	public Route getRegisteredRoute(HttpMethod method, String path){
		return routes.get(method.name()+"_"+path);
	}

}
