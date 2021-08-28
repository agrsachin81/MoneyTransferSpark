
package home.test.restapi.example.moneytransfer;

import static home.test.restapi.testtool.TestRequestHelper.request;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import home.test.restapi.testtool.TestResponse;

public class MoneyTransferAPITestHelper {
	public static final String TRANSFER_TYPE_DEBIT = "debit";
	public static final String TRANSFER_TYPE_CREDIT = "credit";
	private static AtomicInteger accCreateCounter = new AtomicInteger(0);
	private static AtomicInteger transactionCreateCounter = new AtomicInteger(0);

	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_ERROR = "ERROR";
	public static final String DEBIT_FAILED = "DEBIT_FAILED";
	public static final String AMOUNT_TRANSACTED = "amount";
	public static final String CP_ACCOUNT_ID = "cpAccountId";
	public static final String TRANSACTION_REKUEST_ID = "transactionRekuestId";
	public static final String NAME_PREFIX = "john";
	public static final String NAME = "name";
	public static final String BALANCE = "balance";
	public static final String ACCOUNT_ID = "accountId";
	public static final String STATUS = "status";
	public static final String MOBILE_NUMBER = "mobileNumber";
	public static final String TRAN_REK_ID_PREFIX = "TRANS_";

	public static final int MOBILE_NUMBER_START = 1000000;

	public static final double INIT_BALANCE = 300;

	public static void verifyTransactionFailed(String origAccountId, Map<String, Object> json, String cpAccountId,
			double amount, String transactionStatus) {
		int currentCounterValue = transactionCreateCounter.get();
		assertEquals("Status is not correct", STATUS_ERROR, json.get(STATUS));
		assertEquals("Transaction status is not correct", transactionStatus, json.get("transactionStatus"));
		String accId = (String) json.get(ACCOUNT_ID);
		assertNotNull(accId);

		assertEquals("Originating account Id do not match", origAccountId, accId);
		assertEquals("TransactionRek Id does not match ", TRAN_REK_ID_PREFIX + currentCounterValue,
				json.get(TRANSACTION_REKUEST_ID));
		assertEquals("counterparty AccId do not match ", cpAccountId, (String) json.get(CP_ACCOUNT_ID));

		double origAccountBalance = getAccountBalance(origAccountId);
		double cpAccountBalance = getAccountBalance(cpAccountId);
		assertEquals("origAccountBalance ", INIT_BALANCE, origAccountBalance, 0.001);
		assertEquals("origAccountBalance ", INIT_BALANCE, cpAccountBalance, 0.001);
	}

	public static Map<String, Object> verifyAccountDetails(Map<String, Object> json) {
		int currentCounterValue = accCreateCounter.get();
		assertEquals(NAME_PREFIX + currentCounterValue, json.get(NAME));

		assertEquals(String.valueOf(MOBILE_NUMBER_START + currentCounterValue), json.get(MOBILE_NUMBER));
		assertEquals("Status is not correct", STATUS_SUCCESS, json.get(STATUS));
		assertNotNull(json.get(ACCOUNT_ID));
		assertEquals("balance does not match", INIT_BALANCE, (double) json.get(BALANCE), 0.0001);
		return json;
	}

	public static Map<String, Object> createAccountRetJson() {
		int counter = accCreateCounter.incrementAndGet();
		TestResponse res = request("POST", "/account", "{name:john" + counter + ",mobileNumber:"
				+ String.valueOf(MOBILE_NUMBER_START + counter) + ",balance:" + INIT_BALANCE + "}");
		Map<String, Object> json = res.json();
		assertEquals(200, res.status);
		return json;
	}

	public static String createAccountReturnAccountId() {
		Map<String, Object> json = createAccountRetJson();
		return (String) json.get(ACCOUNT_ID);
	}

	public static Map<String, Object> getAccountStatus(String accId) {
		TestResponse response = request("GET", "/account/" + accId, "");
		System.out.println(response.body);
		Map<String, Object> json = response.json();
		assertEquals(200, response.status);
		return json;
	}

	public static double getAccountBalance(String accountId) {

		Map<String, Object> json = getAccountStatus(accountId);

		return (double) json.get(BALANCE);
	}

	public static Map<String, Object> transferAndRetJson(String origAccountId, double amount, String cpAccountId) {
		int counter = transactionCreateCounter.incrementAndGet();

		TestResponse res = request("POST", "/account/" + origAccountId + "/transact", "{amount:" + amount
				+ ",cpAccountId:" + cpAccountId + ",transactionRekuestId:" + TRAN_REK_ID_PREFIX + counter + "}");
		Map<String, Object> json = res.json();
		assertEquals(200, res.status);
		return json;
	}

	public static Map<String, Object> transferCashAndRetJson(String origAccountId, double amount, String transferType) {
		int counter = transactionCreateCounter.incrementAndGet();

		TestResponse res = request("POST", "/account/" + origAccountId + "/transact", "{amount:" + amount
				+ ",transactionRekuestId:" + TRAN_REK_ID_PREFIX + counter + ",transactionType:" + transferType + "}");
		
		Map<String, Object> json = res.json();
		assertEquals(200, res.status);
		return json;
	}

	public static void verifyTransaction(String origAccountId, Map<String, Object> json, String cpAccountId,
			double amount) {
		int currentCounterValue = transactionCreateCounter.get();
		assertEquals("Status is not correct", STATUS_SUCCESS, json.get(STATUS));
		assertEquals("Transaction status is not correct", "DONE", json.get("transactionStatus"));
		String accId = (String) json.get(ACCOUNT_ID);
		assertNotNull(accId);

		assertEquals("origAccId does not match", origAccountId, accId);
		assertEquals("TransactionRek Id do not match ", TRAN_REK_ID_PREFIX + currentCounterValue,
				json.get(TRANSACTION_REKUEST_ID));

		double origAccountBalance = getAccountBalance(origAccountId);
		
		assertEquals("origAccountBalance ", INIT_BALANCE - amount, origAccountBalance, 0.001);

		if (cpAccountId != null) {
			double cpAccountBalance = getAccountBalance(cpAccountId);
			assertEquals("cpAccId does not match ", cpAccountId, (String) json.get(CP_ACCOUNT_ID));
			assertEquals("origAccountBalance ", INIT_BALANCE + amount, cpAccountBalance, 0.001);
		}
	}
}