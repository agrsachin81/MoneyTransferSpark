
package home.test.restapi.example.moneytransfer.service;

import static home.test.restapi.example.moneytransfer.api.RestfulAccountService.PATH_ACCOUNT;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import home.test.api.example.moneytransfer.spi.TransactionService;
import home.test.api.example.moneytransfer.spi.interfaces.AccountResult;
import home.test.api.example.moneytransfer.spi.interfaces.TransactionRekuest;
import home.test.api.example.moneytransfer.spi.interfaces.TransactionResult;
import home.test.api.example.moneytransfer.util.GsonHelper;
import home.test.restapi.example.moneytransfer.MoneyTransferAPI;
import home.test.restapi.example.moneytransfer.api.RestfulTransactService;
import home.test.restapi.example.moneytransfer.mock.MockJsonSerializer;
import home.test.restapi.example.moneytransfer.mock.MockMoneyTransferAbstractServiceFactory;
import home.test.restapi.example.moneytransfer.mock.MockRestfulRouter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.route.HttpMethod;

import static home.test.restapi.example.moneytransfer.service.UnitTestHelper.*;

/**
 * Just to test whether 1) Transaction reuest is correct sent to underlying
 * transaction service 2) result sent by underlying service is correctly
 * forwarded
 * 
 * @author sachin
 *
 */
public class TestRestfulTransactionService {
	private MockRestfulRouter ROUTER;
	private TransactionService TRANSACT_SERVICE;
	private MockJsonSerializer JSON_SERIALIZER;

	@Before
	public void setup() {
		ROUTER = new MockRestfulRouter();
		MockMoneyTransferAbstractServiceFactory serviceAbstractFactory = new MockMoneyTransferAbstractServiceFactory();
		JSON_SERIALIZER = new MockJsonSerializer(GsonHelper.createJsonSerializerDeserializer());
		MoneyTransferAPI api = MoneyTransferAPI.init(ROUTER, serviceAbstractFactory, JSON_SERIALIZER);
		TRANSACT_SERVICE = serviceAbstractFactory.getTransactionService();
	}

	@Test
	public void testSuccessfulTransaction() {
		Route route = ROUTER.getRegisteredRoute(HttpMethod.post, RestfulTransactService.PATH_TRANSACT);
		assertNotNull(route);
		String reuestJSON = "{cpAccountId:accid2,amount:534.6,transactionRekuestId:samRekId1}";
		AtomicReference<TransactionResult> result = new AtomicReference<>(null);
		Mockito.when(TRANSACT_SERVICE.transfer(any(), any())).thenAnswer(invocation -> {
			result.set(convertRekuestToSuccessTransactResult(invocation.getArgument(0),invocation.getArgument(1),"TransReferId"));
			return result.get();
		});
		verifyTransferResult(route, reuestJSON, result, "origAccId1", "accid2", 534.6,"samRekId1");
	}

	@Test
	public void testFailedTransaction() {
		// do a transaction on unknown accounts
		Route route = ROUTER.getRegisteredRoute(HttpMethod.post, RestfulTransactService.PATH_TRANSACT);
		assertNotNull(route);
		String reuestJSON = "{cpAccountId:accid2,amount:124.2,transactionRekuestId:samRekId2}";
		AtomicReference<TransactionResult> result = new AtomicReference<>(null);
		Mockito.when(TRANSACT_SERVICE.transfer(any(), any())).thenAnswer(invocation -> {
			result.set(convertRekuestToFailedTransactResult(invocation.getArgument(0),invocation.getArgument(1),"TransReferId"));
			return result.get();
		});
		verifyTransferResult(route, reuestJSON, result, "origAccId2", "accid2",124.2,"samRekId2");
	}
	
	@Ignore
	@Test
	public void testSuccessGetTransactions() {
		Route route = ROUTER.getRegisteredRoute(HttpMethod.post, RestfulTransactService.PATH_TRANSACT);
		assertNotNull(route);
		String reuestJSON = "{cpAccountId:accid2,amount:534.6,transactionRekuestId:samRekId1}";
		AtomicReference<TransactionResult> result = new AtomicReference<>(null);
		Mockito.when(TRANSACT_SERVICE.transfer(any(), any())).thenAnswer(invocation -> {
			result.set(convertRekuestToSuccessTransactResult(invocation.getArgument(0),invocation.getArgument(1),"TransReferId"));
			return result.get();
		});
		verifyTransferResult(route, reuestJSON, result, "origAccId1", "accid2", 534.6,"samRekId1");
	}

	@Ignore
	@Test
	public void testFailedGetTransactions() {
		// for an invalid account id
	}

	@Ignore
	@Test
	public void testSuccessFulTransactionStatusGet() {
		// known transaction Id
		// create accounts
		// make a transaction
	}

	@Ignore
	@Test
	public void testFailedTransactionStatusGet() {
		// unknown transaction Id
	}

	

	@Ignore
	@Test
	public void testGetTransactionEmpty() {
		// when account id is valid but there is no transaction on that account
	}

	@Ignore
	@Test
	public void testDeleteTransactionIsSentToUnderlying() {
		// test the result returned from underlying is correct sent
	}

	@Ignore
	@Test
	public void testUpdateTransactionisSentToUnderlying() {
		// transaction cn not be update
	}
	
	private void verifyTransferResult(Route route,String reuestJSON, AtomicReference<TransactionResult> result, String origAccountId,
			String cpAccId, double amount, String rekuestId) {
		Request request = createMockRekuest(reuestJSON, HttpMethod.post.name(), origAccountId);
		Response response = createMockResponse();
		try {
			String json = (String) route.handle(request, response);
			Mockito.verify(response).type(STRING_CAPTOR.capture());
			Mockito.verify(TRANSACT_SERVICE).transfer(TRANSFER_REKUEST_CAPTOR.capture(), ORIGINATING_CAPTOR.capture());
			verifyTransactionRekuest(TRANSFER_REKUEST_CAPTOR, ORIGINATING_CAPTOR, origAccountId, amount, cpAccId, rekuestId);
			verifyResponseType(response);
			verifyTransactResult(JSON_SERIALIZER.getToJsonLastObject(), result.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
