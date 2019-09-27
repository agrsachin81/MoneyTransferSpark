package home.test.restapi.example.moneytransfer;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import com.google.gson.Gson;

import home.test.api.example.moneytransfer.service.impl.MoneyTransferInMemoryServiceFactory;
import home.test.api.example.moneytransfer.spi.AccountService;
import home.test.api.example.moneytransfer.spi.MoneyTransferAbstractServiceFactory;
import home.test.api.example.moneytransfer.spi.TransactionService;
import home.test.restapi.example.moneytransfer.api.RestfulAccountService;
import home.test.restapi.example.moneytransfer.api.RestfulTransactService;
import home.test.restapi.routing.RestfulRouter;
import home.test.restapi.routing.SparkRestfulRouter;
import home.test.restapi.utils.GoogleJsonSerializer;
import home.test.restapi.utils.JsonSerializer;

public class MoneyTransferAPI {

	private static void registerHomePage() {
		get("/", (rek, res) -> " WIP WIP");
	}

	public static void main(String[] args) {
		port(1010);
		registerHomePage();
		SparkRestfulRouter router = new SparkRestfulRouter();
		MoneyTransferInMemoryServiceFactory serviceAbstractFactory = MoneyTransferInMemoryServiceFactory.getInstance();
		JsonSerializer serializer= new GoogleJsonSerializer(serviceAbstractFactory.getJson());
		init(router, serviceAbstractFactory,  serializer);
	}

	public static MoneyTransferAPI init(RestfulRouter router,
			MoneyTransferAbstractServiceFactory serviceAbstractFactory, JsonSerializer serializer) {
		MoneyTransferAPI api = new MoneyTransferAPI(serviceAbstractFactory, router, serializer);
		return api;
	}

	public MoneyTransferAPI(MoneyTransferAbstractServiceFactory serviceAbstractFactory, RestfulRouter router, JsonSerializer serializer) {
		this.accountService = serviceAbstractFactory.getAccountService();
		this.transactionService = serviceAbstractFactory.getTransactionService();
		restFulAccountservice = new RestfulAccountService(serializer, accountService, router);
		restFulTransactservice = new RestfulTransactService(serializer, transactionService, router);
	}

	private final AccountService accountService;
	private final TransactionService transactionService;
	final RestfulAccountService restFulAccountservice;
	final RestfulTransactService restFulTransactservice;

	public RestfulAccountService getRestFulAccountservice() {
		return restFulAccountservice;
	}

	public RestfulTransactService getRestFulTransactservice() {
		return restFulTransactservice;
	}

}
