
package home.test.restapi.example.moneytransfer.mock;

import org.mockito.Mockito;

import com.google.gson.Gson;

import home.test.api.example.moneytransfer.spi.AccountService;
import home.test.api.example.moneytransfer.spi.MoneyTransferAbstractServiceFactory;
import home.test.api.example.moneytransfer.spi.TransactionService;

public class MockMoneyTransferAbstractServiceFactory implements MoneyTransferAbstractServiceFactory{

	public MockMoneyTransferAbstractServiceFactory() {
		mockAccService = Mockito.mock(AccountService.class);
		mockTransService = Mockito.mock(TransactionService.class);
	}
	
	AccountService mockAccService;
	TransactionService mockTransService;
	
	@Override
	public AccountService getAccountService() {
		return mockAccService;
	}

	@Override
	public TransactionService getTransactionService() {
		return mockTransService;
	}

	@Override
	public Gson getJson() {
		return Mockito.mock(Gson.class);
	}
}
