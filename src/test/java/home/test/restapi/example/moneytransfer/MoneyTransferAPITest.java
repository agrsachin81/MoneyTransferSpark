
package home.test.restapi.example.moneytransfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import home.test.api.example.moneytransfer.util.GsonHelper;
import home.test.restapi.example.moneytransfer.mock.MockJsonSerializer;
import home.test.restapi.example.moneytransfer.mock.MockMoneyTransferAbstractServiceFactory;
import home.test.restapi.example.moneytransfer.mock.MockRestfulRouter;

/**
 * Need to test the routing code here written internally
 * internal wiring is correct or not
 * @author sachin
 *
 * Type MoneyTransferAPITest, created on 22-Sep-2019 at 5:15:33 pm
 *
 */
public class MoneyTransferAPITest {

	public static MoneyTransferAPI api ;
	public static MockRestfulRouter router;
	public static MockMoneyTransferAbstractServiceFactory serviceAbstractFactory;
	public static MockJsonSerializer jsonSerializer;
	
	@BeforeClass
	public static void setup() {
		router = new MockRestfulRouter();
		serviceAbstractFactory = new MockMoneyTransferAbstractServiceFactory();
		jsonSerializer = new MockJsonSerializer( GsonHelper.createJsonSerializerDeserializer());
		api = MoneyTransferAPI.init(router, serviceAbstractFactory, jsonSerializer);
	}
	
	@Test
	public void testRestFulAccountservice() {
		assertNotNull(" restful account service must not be null", api.getRestFulAccountservice());
		assertEquals(" there is a different router object ", router, api.getRestFulAccountservice().getRouter());
		assertEquals(" there is a different Service object ", serviceAbstractFactory.getAccountService(), api.getRestFulAccountservice().getAccountService());
	}

	@Test
	public void testRestfulTransactionService() {
		assertNotNull(" restful account service must not be null", api.getRestFulTransactservice());
		assertEquals(" there is a different router object ", router, api.getRestFulTransactservice().getRouter());
		assertEquals(" there is a different Service object ", serviceAbstractFactory.getTransactionService(), api.getRestFulTransactservice().getTransactionService());
	}
}
