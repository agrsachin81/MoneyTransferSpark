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
import home.test.api.example.moneytransfer.spi.utils.AccountRekuestImpl;
import home.test.api.example.moneytransfer.spi.utils.TransactionRekuestImpl;

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

		AccountRekuestImpl account = gson.fromJson(rek, AccountRekuestImpl.class);

		assertNotNull("JSON TO OBJ CONVERSION FAILED |" + rek + "|", account);
		assertNotNull("blance should be zero", account.getBalance());
		assertNull("accointId must be null", account.getAccountId());
	}

	@Test
	public void transactConverionTest() {
		String rek = "{\"amount\":100.0,\"cpAccountId\":\"PA_2\",\"transactionRekuestId\":\"abcd\"}";

		TransactionRekuestImpl transaction = gson.fromJson(rek, TransactionRekuestImpl.class);

		assertNotNull("JSON TO OBJ CONVERSION FAILED |" + rek + "|", transaction);
		assertEquals("amount is not parsed", 100.0, transaction.getAmount(), 0.0000000001);
		assertEquals("CounterParty is not parsed Correctly", "PA_2", transaction.getCpAccountId().get());
		assertEquals("default type is not correct", TransactionType.DEBIT_ACCOUNT, transaction.getTransactionType());
		String expected = "abcd".intern();
		assertEquals("transactionId not parsed", expected, transaction.getTransactionRekuestId());
	}
	
	@Test
	public void transactConverionTestDebitCash() {
		String rek = "{\"amount\":100.0,\"transactionType\":\"debit\",\"transactionRekuestId\":\"abcd\"}";

		TransactionRekuestImpl transaction = gson.fromJson(rek, TransactionRekuestImpl.class);

		assertNotNull("JSON TO OBJ CONVERSION FAILED |" + rek + "|", transaction);
		assertEquals("amount is not parsed", 100.0, transaction.getAmount(), 0.0000000001);
		assertEquals("CounterParty is not parsed Correctly", Optional.empty(), transaction.getCpAccountId());
		assertEquals("default type is not correct", TransactionType.DEBIT_CASH, transaction.getTransactionType());
		String expected = "abcd".intern();
		assertEquals("transactionId not parsed", expected, transaction.getTransactionRekuestId());
	}
}
