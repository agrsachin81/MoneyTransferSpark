
package home.test.restapi.example.moneytransfer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import home.test.api.example.moneytransfer.spi.enums.AccountStatus;
import home.test.api.example.moneytransfer.spi.enums.StatusResponse;
import home.test.api.example.moneytransfer.spi.enums.TransactionStatus;
import home.test.api.example.moneytransfer.spi.interfaces.AccountRekuest;
import home.test.api.example.moneytransfer.spi.interfaces.AccountResult;
import home.test.api.example.moneytransfer.spi.interfaces.TransactionRekuest;
import home.test.api.example.moneytransfer.spi.interfaces.TransactionResult;
import spark.Request;
import spark.Response;

public class UnitTestHelper {
	// TODO:make variable threadlocal to ensure when tests run in parallel
	public static ArgumentCaptor<AccountRekuest> ACCOUNT_REKUEST_CAPTOR = ArgumentCaptor.forClass(AccountRekuest.class);

	public static ArgumentCaptor<TransactionRekuest> TRANSFER_REKUEST_CAPTOR = ArgumentCaptor
			.forClass(TransactionRekuest.class);
	public static ArgumentCaptor<String> ORIGINATING_CAPTOR = ArgumentCaptor.forClass(String.class);

	public static ArgumentCaptor<Object> JSON_CAPTOR = ArgumentCaptor.forClass(Object.class);

	public static ArgumentCaptor<String> STRING_CAPTOR = ArgumentCaptor.forClass(String.class);
	public static ArgumentMatcher<String> IS_JSON_EVALUATOR = type -> type.equals("application/json");

	public static Response createMockResponse() {
		Response reposne = Mockito.mock(Response.class);

		return reposne;
	}

	public static void verifyResponseType(Response response) {
		// in real life response type could be xml, json and any other as expected by
		// client
		assertEquals("type must always be application/json", true, IS_JSON_EVALUATOR.matches(STRING_CAPTOR.getValue()));
	}

	public static Request createMockRekuest(String body, String method, String... splat) {

		// TODO:
		// set Content-type: application/json
		Request rekuest = Mockito.mock(Request.class);

		Mockito.when(rekuest.body()).thenReturn(body);
		Mockito.when(rekuest.requestMethod()).thenReturn(method);
		Mockito.when(rekuest.splat()).thenReturn(splat);

		return rekuest;
	}

	public static void verifyAccounRekuest(AccountRekuest rekuest, String name, String mobileNumber, double balance) {
		assertEquals("balance is incorrect parsed", balance, rekuest.getBalance(), 0.00001);
		assertEquals("name is incorrect parsed", name, rekuest.getName());
		assertEquals("mobileNumber is incorrect parsed", mobileNumber, rekuest.getMobileNumber());
	}

	public static void verifyTransactionRekuest(ArgumentCaptor<TransactionRekuest> rekuestCapture,
			ArgumentCaptor<String> originAccntRekuest, String originAccountId, double amount, String cpAccountId,String rekuestId) {
		TransactionRekuest rekuest = rekuestCapture.getValue();
		String origAccntIdRekuested = originAccntRekuest.getValue();
		assertEquals("Amount is incorrect parsed", amount, rekuest.getAmount(), 0.00001);
		assertEquals("cpAccountId is incorrect parsed", cpAccountId, rekuest.getCpAccountId().get());
		assertEquals("orignAccountId is incorrect parsed", originAccountId, origAccntIdRekuested);
		assertEquals("TransactionRekuestId is incorrect parsed", rekuest.getTransactionRekuestId(), rekuestId);
	}

	public static void verifyAccountResult(Object json, AccountResult result) {
		assertEquals("AccountResult does not match", result, json);
	}

	public static void verifyTransactResult(Object json, TransactionResult result) {
		assertEquals("TransactResult does not match", result, json);
	}

	public static TransactionResult convertRekuestToSuccessTransactResult(TransactionRekuest rekuest,
			String origAccntId, String transrefeId) {

		assertNotNull(rekuest);
		TransactionResult result = mock(TransactionResult.class);
		when(result.getAccountId()).thenReturn(origAccntId);
		when(result.getAmount()).thenReturn(rekuest.getAmount());
		when(result.getCashReferenceId()).thenReturn(rekuest.getCashReferenceId());
		when(result.getStatus()).thenReturn(StatusResponse.SUCCESS);
		when(result.getTransactionStatus()).thenReturn(TransactionStatus.DONE);
		when(result.getCpAccountId()).thenReturn(rekuest.getCpAccountId());
		when(result.getTransactionRekuestId()).thenReturn(rekuest.getTransactionRekuestId());
		when(result.getTransactionReferenceId()).thenReturn(transrefeId);
		when(result.getTimeStamp()).thenReturn(System.currentTimeMillis());
		when(result.getMessage()).thenReturn("Transaction Successfull");
		return result;
	}
	
	public static TransactionResult convertRekuestToFailedTransactResult(TransactionRekuest rekuest,
			String origAccntId, String transrefeId) {

		assertNotNull(rekuest);
		TransactionResult result = mock(TransactionResult.class);
		when(result.getAccountId()).thenReturn(origAccntId);
		when(result.getAmount()).thenReturn(rekuest.getAmount());
		when(result.getCashReferenceId()).thenReturn(rekuest.getCashReferenceId());
		when(result.getStatus()).thenReturn(StatusResponse.ERROR);
		when(result.getTransactionStatus()).thenReturn(TransactionStatus.ABORTED);
		when(result.getCpAccountId()).thenReturn(rekuest.getCpAccountId());
		when(result.getTransactionRekuestId()).thenReturn(rekuest.getTransactionRekuestId());
		when(result.getTransactionReferenceId()).thenReturn(transrefeId);
		when(result.getTimeStamp()).thenReturn(System.currentTimeMillis());
		when(result.getMessage()).thenReturn("Transaction unsuccessful");
		return result;
	}

	public static AccountResult convertRekuestToSuccessAccountResult(String accId, AccountRekuest rekuest) {

		assertNotNull(rekuest);
		AccountResult result = mock(AccountResult.class);
		when(result.getAccountId()).thenReturn(accId);
		when(result.getName()).thenReturn(rekuest.getName());
		when(result.getMobileNumber()).thenReturn(rekuest.getMobileNumber());
		when(result.getStatus()).thenReturn(StatusResponse.SUCCESS);
		when(result.getAccountStatus()).thenReturn(AccountStatus.CREATED);
		when(result.getBalance()).thenReturn(rekuest.getBalance());
		when(result.getMessage()).thenReturn("Account Created Successfully");
		return result;
	}

	public static AccountResult convertRekuestToFailedAccountResult(AccountRekuest rekuest) {

		assertNotNull(rekuest);
		AccountResult result = mock(AccountResult.class);
		when(result.getAccountId()).thenReturn(null);
		when(result.getName()).thenReturn(rekuest.getName());
		when(result.getMobileNumber()).thenReturn(rekuest.getMobileNumber());
		when(result.getStatus()).thenReturn(StatusResponse.ERROR);
		when(result.getAccountStatus()).thenReturn(AccountStatus.UNKNOWN);
		when(result.getBalance()).thenReturn(0.0);
		when(result.getMessage()).thenReturn("Unable to create Account");
		return result;
	}
}
