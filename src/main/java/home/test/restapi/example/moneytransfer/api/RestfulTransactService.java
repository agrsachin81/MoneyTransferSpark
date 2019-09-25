
package home.test.restapi.example.moneytransfer.api;

import java.util.Collection;

import home.test.api.example.moneytransfer.spi.TransactionService;
import home.test.api.example.moneytransfer.spi.interfaces.TransactionRekuest;
import home.test.api.example.moneytransfer.spi.interfaces.TransactionResult;
import home.test.api.example.moneytransfer.spi.utils.TransactionRekuestImpl;
import home.test.api.example.moneytransfer.util.StandardResponse;
import home.test.restapi.routing.RestfulRouter;
import home.test.restapi.utils.JsonSerializer;

public class RestfulTransactService {

	private static final String ACC_ID_FOR_TRANSACT = "*";

	private static final String TRANS_ID_PATH_PARAM = ":id";

	public static final String PATH_TRANSACT = RestfulAccountService.PATH_ACCOUNT_FWD_SLASH + ACC_ID_FOR_TRANSACT
			+ "/transact";
	public static final String PATH_TRANSACT_FWD_SLASH = PATH_TRANSACT + "/";
	public static final String PATH_TRANSACT_ID = PATH_TRANSACT_FWD_SLASH + TRANS_ID_PATH_PARAM;

	private final RestfulRouter router;
	private final TransactionService transactionService;
	private final JsonSerializer gson;

	public RestfulTransactService(JsonSerializer serializer, TransactionService transactService, RestfulRouter router) {
		this.router = router;
		this.gson = serializer;
		this.transactionService = transactService;
		registerRoutes();
	}

	public RestfulRouter getRouter() {
		return router;
	}

	public TransactionService getTransactionService() {
		return transactionService;
	}

	private void registerRoutes() {
		router.post("/account/*/transact", (request, response) -> {
			
			response.type("application/json");
			TransactionRekuest transaction = gson.fromJson(request.body(), TransactionRekuestImpl.class);
			TransactionResult result = transactionService.transfer(transaction, request.splat()[0]);

			return gson.toJson(result);
		});

		router.get("/account/*/transact", (request, response) -> {
			response.type("application/json");
			Collection<TransactionResult> result = transactionService.getTransactions(request.splat()[0], 10);

			return gson.toJsonTree(result);
		});

		router.get("/account/*/transact/" + TRANS_ID_PATH_PARAM, (rekuest, response) -> {

			response.type("application/json");
			TransactionResult result = transactionService.getTransaction(rekuest.splat()[0], rekuest.splat()[1]);
			return gson.toJson(result);
		});

		router.put("/account/*/transact/" + TRANS_ID_PATH_PARAM, (rekuest, response) -> {
			response.type("application/json");
			return StandardResponse.ERROR_RESPONSE_JSON;

		});
		
		router.delete("/account/*/transact/" + TRANS_ID_PATH_PARAM, (rekuest, response) -> {
			response.type("application/json");
			return StandardResponse.ERROR_RESPONSE_JSON;

		});
		
		router.post("/account/*/transact/" + TRANS_ID_PATH_PARAM, (rekuest, response) -> {
			response.type("application/json");
			return StandardResponse.ERROR_RESPONSE_JSON;

		});
	}
}
