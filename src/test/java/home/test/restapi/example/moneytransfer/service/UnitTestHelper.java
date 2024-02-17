
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
import home.test.api.example.moneytransfer.spi.interfaces.AccountRequest;
import home.test.api.example.moneytransfer.spi.interfaces.AccountResult;
import home.test.api.example.moneytransfer.spi.interfaces.TransactionRequest;
import home.test.api.example.moneytransfer.spi.interfaces.TransactionResult;
import spark.Request;
import spark.Response;

public class UnitTestHelper {
	// TODO:make variable threadlocal to ensure when tests run in parallel
	public static ArgumentCaptor<AccountRequest> ACCOUNT_REQUEST_CAPTOR = ArgumentCaptor.forClass(AccountRequest.class);

	public static ArgumentCaptor<TransactionRequest> TRANSFER_REQUEST_CAPTOR = ArgumentCaptor
			.forClass(TransactionRequest.class);
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

	public static Request createMockRequest(String body, String method, String... splat) {

		// TODO:
		// set Content-type: application/json
		Request request = Mockito.mock(Request.class);

		Mockito.when(request.body()).thenReturn(body);
		Mockito.when(request.requestMethod()).thenReturn(method);
		Mockito.when(request.splat()).thenReturn(splat);

		return request;
	}

	public static void verifyAccountRequest(AccountRequest request, String name, String mobileNumber, double balance) {
		assertEquals("balance is incorrect parsed", balance, request.getBalance(), 0.00001);
		assertEquals("name is incorrect parsed", name, request.getName());
		assertEquals("mobileNumber is incorrect parsed", mobileNumber, request.getMobileNumber());
	}

	public static void verifyTransactionRequest(ArgumentCaptor<TransactionRequest> requestCapture,
												ArgumentCaptor<String> originAccountRequest, String originAccountId, double amount, String cpAccountId, String rekuestId) {
		TransactionRequest request = requestCapture.getValue();
		String origAccidentRequested = originAccountRequest.getValue();
		assertEquals("Amount is incorrect parsed", amount, request.getAmount(), 0.00001);
		assertEquals("cpAccountId is incorrect parsed", cpAccountId, request.getCpAccountId().get());
		assertEquals("orignAccountId is incorrect parsed", originAccountId, origAccidentRequested);
		assertEquals("TransactionRequestId is incorrect parsed", request.getTransactionRequestId(), rekuestId);
	}

	public static void verifyAccountResult(Object json, AccountResult result) {
		assertEquals("AccountResult does not match", result, json);
	}

	public static void verifyTransactResult(Object json, TransactionResult result) {
		assertEquals("TransactResult does not match", result, json);
	}

	public static TransactionResult convertRequestToSuccessTransactResult(TransactionRequest request,
																		  String origAccntId, String transrefeId) {

		assertNotNull(request);
		TransactionResult result = mock(TransactionResult.class);
		when(result.getAccountId()).thenReturn(origAccntId);
		when(result.getAmount()).thenReturn(request.getAmount());
		when(result.getCashReferenceId()).thenReturn(request.getCashReferenceId());
		when(result.getStatus()).thenReturn(StatusResponse.SUCCESS);
		when(result.getTransactionStatus()).thenReturn(TransactionStatus.DONE);
		when(result.getCpAccountId()).thenReturn(request.getCpAccountId());
		when(result.getTransactionRekuestId()).thenReturn(request.getTransactionRequestId());
		when(result.getTransactionReferenceId()).thenReturn(transrefeId);
		when(result.getTimeStamp()).thenReturn(System.currentTimeMillis());
		when(result.getMessage()).thenReturn("Transaction Successfull");
		return result;
	}
	
	public static TransactionResult convertRequestToFailedTransactResult(TransactionRequest rekuest,
																		 String origAccntId, String transrefeId) {

		assertNotNull(rekuest);
		TransactionResult result = mock(TransactionResult.class);
		when(result.getAccountId()).thenReturn(origAccntId);
		when(result.getAmount()).thenReturn(rekuest.getAmount());
		when(result.getCashReferenceId()).thenReturn(rekuest.getCashReferenceId());
		when(result.getStatus()).thenReturn(StatusResponse.ERROR);
		when(result.getTransactionStatus()).thenReturn(TransactionStatus.ABORTED);
		when(result.getCpAccountId()).thenReturn(rekuest.getCpAccountId());
		when(result.getTransactionRekuestId()).thenReturn(rekuest.getTransactionRequestId());
		when(result.getTransactionReferenceId()).thenReturn(transrefeId);
		when(result.getTimeStamp()).thenReturn(System.currentTimeMillis());
		when(result.getMessage()).thenReturn("Transaction unsuccessful");
		return result;
	}

	public static AccountResult convertRequestToSuccessAccountResult(String accId, AccountRequest request) {

		assertNotNull(request);
		AccountResult result = mock(AccountResult.class);
		when(result.getAccountId()).thenReturn(accId);
		when(result.getName()).thenReturn(request.getName());
		when(result.getMobileNumber()).thenReturn(request.getMobileNumber());
		when(result.getStatus()).thenReturn(StatusResponse.SUCCESS);
		when(result.getAccountStatus()).thenReturn(AccountStatus.CREATED);
		when(result.getBalance()).thenReturn(request.getBalance());
		when(result.getMessage()).thenReturn("Account Created Successfully");
		return result;
	}

	public static AccountResult convertRekuestToFailedAccountResult(AccountRequest request) {

		assertNotNull(request);
		AccountResult result = mock(AccountResult.class);
		when(result.getAccountId()).thenReturn(null);
		when(result.getName()).thenReturn(request.getName());
		when(result.getMobileNumber()).thenReturn(request.getMobileNumber());
		when(result.getStatus()).thenReturn(StatusResponse.ERROR);
		when(result.getAccountStatus()).thenReturn(AccountStatus.UNKNOWN);
		when(result.getBalance()).thenReturn(0.0);
		when(result.getMessage()).thenReturn("Unable to create Account");
		return result;
	}
}
