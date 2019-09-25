
package home.test.restapi.example.moneytransfer.service;

import static home.test.restapi.example.moneytransfer.api.RestfulAccountService.PATH_ACCOUNT;
import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.ACCOUNT_REKUEST_CAPTOR;
import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.STRING_CAPTOR;
import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.convertRekuestToFailedAccountResult;
import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.convertRekuestToSuccessAccountResult;
import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.createMockRekuest;
import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.createMockResponse;
import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.verifyAccounRekuest;
import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.verifyAccountResult;
import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.verifyResponseType;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import home.test.api.example.moneytransfer.spi.AccountService;
import home.test.api.example.moneytransfer.spi.interfaces.AccountResult;
import home.test.api.example.moneytransfer.util.GsonHelper;
import home.test.restapi.example.moneytransfer.MoneyTransferAPI;
import home.test.restapi.example.moneytransfer.mock.MockJsonSerializer;
import home.test.restapi.example.moneytransfer.mock.MockMoneyTransferAbstractServiceFactory;
import home.test.restapi.example.moneytransfer.mock.MockRestfulRouter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.route.HttpMethod;

/**
 * Just to test whether the rekuest is correctly routed to underlying account
 * service result returned by AccountService is correctly forwarded by restful
 * api routing logic
 * 
 * @author sachin
 *
 *         Type TestRestfulAccountService, created on 22-Sep-2019 at 6:53:57 pm
 *
 */
public class TestRestfulAccountService {

	MockRestfulRouter ROUTER;
	AccountService ACCOUNT_SERVICE;
	private MockJsonSerializer JSON_SERIALIZER;

	@Before
	public void setup() {
		ROUTER = new MockRestfulRouter();
		MockMoneyTransferAbstractServiceFactory serviceAbstractFactory = new MockMoneyTransferAbstractServiceFactory();
		JSON_SERIALIZER = new MockJsonSerializer(GsonHelper.createJsonSerializerDeserializer());
		MoneyTransferAPI api = MoneyTransferAPI.init(ROUTER, serviceAbstractFactory, JSON_SERIALIZER);
		ACCOUNT_SERVICE = serviceAbstractFactory.getAccountService();
	}

	@Test
	public void testAddAccountSucceed() {
		Route route = ROUTER.getRegisteredRoute(HttpMethod.post, PATH_ACCOUNT);

		assertNotNull(route);
		String reuestJSON = "{name:john,mobileNumber:12345567908}";
		AtomicReference<AccountResult> result = new AtomicReference<>(null);
		Mockito.when(ACCOUNT_SERVICE.addAccount(any())).thenAnswer(invocation -> {
			result.set(convertRekuestToSuccessAccountResult("someAccntId", invocation.getArgument(0)));
			return result.get();
		});
		verifyAddAccountResult(reuestJSON, result, "john", "12345567908", 0.0, route);
	}

	@Test
	public void testAddAccountFailed() {
		Route route = ROUTER.getRegisteredRoute(HttpMethod.post, PATH_ACCOUNT);
		assertNotNull(route);
		String reuestJSON = "{name:john2,mobileNumber:12345767908}";
		AtomicReference<AccountResult> addAccountresult = new AtomicReference<>(null);
		Mockito.when(ACCOUNT_SERVICE.addAccount(any())).thenAnswer(invocation -> {
			addAccountresult.set(convertRekuestToFailedAccountResult(invocation.getArgument(0)));
			return addAccountresult.get();
		});
		
		verifyAddAccountResult(reuestJSON, addAccountresult,  "john2", "12345767908", 0.0, route);
	}

	private void verifyAddAccountResult(String reuestJSON, AtomicReference<AccountResult> result, String name,
			String mobileNumber, double balance, Route route) {
		
		Request request = createMockRekuest(reuestJSON, HttpMethod.post.name());
		Response response = createMockResponse();
		try {
			String json = (String) route.handle(request, response);
			Mockito.verify(response).type(STRING_CAPTOR.capture());
			Mockito.verify(ACCOUNT_SERVICE).addAccount(ACCOUNT_REKUEST_CAPTOR.capture());
			verifyAccounRekuest(ACCOUNT_REKUEST_CAPTOR.getValue(), name, mobileNumber, balance);
			verifyResponseType(response);
			verifyAccountResult(JSON_SERIALIZER.getToJsonLastObject(), result.get());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void testGetAccountSucceed() {

	}

	@Ignore
	@Test
	public void testGetAccountFailed() {

	}

	@Ignore
	@Test
	public void testGetAccountsSucceed() {

	}

	@Ignore
	@Test
	public void testGetAccountsFailed() {

	}

	@Ignore
	@Test
	public void testDeleteAccountSuccess() {

	}

	@Ignore
	@Test
	public void testDeleteAccountFailed() {

	}
}
