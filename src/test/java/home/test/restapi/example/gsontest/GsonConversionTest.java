package home.test.restapi.example.gsontest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import home.test.api.example.moneytransfer.service.impl.MoneyTransferInMemoryServiceFactory;
import home.test.api.example.moneytransfer.spi.enums.TransactionType;
import home.test.api.example.moneytransfer.spi.utils.AccountRequestImpl;
import home.test.api.example.moneytransfer.spi.utils.TransactionRequestImpl;

public class GsonConversionTest {

	static Gson gson;

	@BeforeClass
	public static void setup() {
		MoneyTransferInMemoryServiceFactory moneyTransferInMemoryServiceFactory = MoneyTransferInMemoryServiceFactory.getInstance();
		gson = moneyTransferInMemoryServiceFactory.getJson();
	}

	@Test
	public void defaultConversionTest() {
		String rek = "{name:sachin,mobileNumber:9876544}";

		AccountRequestImpl account = gson.fromJson(rek, AccountRequestImpl.class);
		assertNotNull("JSON TO OBJ CONVERSION FAILED |" + rek + "|", account);
		assertEquals(0.0, account.getBalance() ,0.00001);
		assertNull("accountId must be null", account.getAccountId());
	}

	@Test
	public void transactConversionTest() {
		String rek = "{\"amount\":100.0,\"cpAccountId\":\"PA_2\",\"transactionRequestId\":\"abcd\"}";

		TransactionRequestImpl transaction = gson.fromJson(rek, TransactionRequestImpl.class);

		assertNotNull("JSON TO OBJ CONVERSION FAILED |" + rek + "|", transaction);
		assertEquals("amount is not parsed", 100.0, transaction.getAmount(), 0.0000000001);
		assertEquals("CounterParty is not parsed Correctly", "PA_2", transaction.getCpAccountId().get());
		assertEquals("default type is not correct", TransactionType.DEBIT_ACCOUNT, transaction.getTransactionType());
		String expected = "abcd".intern();
		assertEquals("transactionId not parsed", expected, transaction.getTransactionRequestId());
	}
	
	@Test
	public void transactConversionTestDebitCash() {
		String rek = "{\"amount\":100.0,\"transactionType\":\"debit\",\"transactionRequestId\":\"abcd\"}";

		TransactionRequestImpl transaction = gson.fromJson(rek, TransactionRequestImpl.class);

		assertNotNull("JSON TO OBJ CONVERSION FAILED |" + rek + "|", transaction);
		assertEquals("amount is not parsed", 100.0, transaction.getAmount(), 0.0000000001);
		assertEquals("CounterParty is not parsed Correctly", Optional.empty(), transaction.getCpAccountId());
		assertEquals("default type is not correct", TransactionType.DEBIT_CASH, transaction.getTransactionType());
		String expected = "abcd".intern();
		assertEquals("transactionId not parsed", expected, transaction.getTransactionRequestId());
	}
}
