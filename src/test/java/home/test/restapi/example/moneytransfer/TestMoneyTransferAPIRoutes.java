
package home.test.restapi.example.moneytransfer;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import home.test.restapi.example.moneytransfer.mock.MockRestfulRouter;
import spark.Route;
import spark.route.HttpMethod;

import static home.test.restapi.example.moneytransfer.api.RestfulAccountService.*;
import static home.test.restapi.example.moneytransfer.api.RestfulTransactService.*;

/**
 * just to test whether all the exposed routes are correctly published by spark Api router
 * @author sachin
 *
 * Type TestMoneyTransferAPIRoutes, created on 22-Sep-2019 at 6:53:23 pm
 *
 */
public class TestMoneyTransferAPIRoutes {

	
	static MockRestfulRouter router ;
	@BeforeClass
	public static void setup() {
		MoneyTransferAPITest.setup();
		router = MoneyTransferAPITest.router;
	}
	
	@Test
	public void testAccountRoutesSet(){
		Route route = router.getRegisteredRoute(HttpMethod.get, PATH_ACCOUNT);
		assertNotNull("Get All Accounts call is not registered ", route);
		
		Route route2 = router.getRegisteredRoute(HttpMethod.post, PATH_ACCOUNT);
		assertNotNull("Create Account call is not registered ", route2);
		
		//todo: make it mroe generic id part should not be hardcoded
		Route route3 = router.getRegisteredRoute(HttpMethod.get, PATH_ACCOUNT_ID);
		assertNotNull("Get account status with id call is not registered ", route3);
		
		Route route4 = router.getRegisteredRoute(HttpMethod.put, PATH_ACCOUNT_ID);
		assertNotNull("Account status with id call is not registered ", route4);
		
		Route route5 = router.getRegisteredRoute(HttpMethod.delete, PATH_ACCOUNT_ID);
		assertNotNull("Account status with id call is not registered ", route5);
	}
	
	@Test
	public void testTransactRoutesSet() {
		Route route = router.getRegisteredRoute(HttpMethod.get, PATH_TRANSACT);
		assertNotNull("get last n Transactions for an account is not registered ", route);
		
		Route route2 = router.getRegisteredRoute(HttpMethod.post, PATH_TRANSACT);
		assertNotNull("Make Transaction on a account is not registered ", route2);
		
		//todo: make it mroe generic id part should not be hardcoded
		Route route3 = router.getRegisteredRoute(HttpMethod.get,PATH_TRANSACT_ID);
		assertNotNull("Get transaction status with id call is not registered ", route3);
		
		Route route4 = router.getRegisteredRoute(HttpMethod.put, PATH_TRANSACT_ID);
		assertNotNull("Update (PUT) Transaction call is not registered ", route4);
		
		Route route5 = router.getRegisteredRoute(HttpMethod.post, PATH_TRANSACT_ID);
		assertNotNull("Update (POST) Transaction call is not registered", route5);
		
		Route route6 = router.getRegisteredRoute(HttpMethod.delete, PATH_TRANSACT_ID);
		assertNotNull("DELETE Transaction call is not registered", route6);
	}
}
