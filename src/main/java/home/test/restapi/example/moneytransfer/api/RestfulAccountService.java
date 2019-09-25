
package home.test.restapi.example.moneytransfer.api;


import home.test.api.example.moneytransfer.spi.AccountService;
import home.test.api.example.moneytransfer.spi.enums.StatusResponse;
import home.test.api.example.moneytransfer.spi.interfaces.AccountResult;
import home.test.api.example.moneytransfer.spi.utils.AccountRekuestImpl;
import home.test.api.example.moneytransfer.util.StandardResponse;
import home.test.restapi.routing.RestfulRouter;
import home.test.restapi.utils.JsonSerializer;

public class RestfulAccountService {

	public static final String PATH_ACCOUNT = "/account";
	public static final String PATH_ACCOUNT_FWD_SLASH =PATH_ACCOUNT+"/";
	private static final String ACC_ID_PATH_PARAM = ":id";
	public static final String PATH_ACCOUNT_ID = PATH_ACCOUNT_FWD_SLASH +ACC_ID_PATH_PARAM;
	
	private final RestfulRouter router;
	private final AccountService accountService;
	private JsonSerializer gson;

	public RestfulRouter getRouter() {
		return router;
	}
	
	public AccountService getAccountService() {
		return accountService;
	}
	
	public RestfulAccountService(JsonSerializer serializer, AccountService accountService, RestfulRouter router) {
		this.router = router;
		this.gson = serializer;
		this.accountService = accountService;
		registerRoutes();
	}

	private void registerRoutes() {
		router.post("/account", (request, response) -> {
			response.type("application/json");

			AccountRekuestImpl account = gson.fromJson(request.body(), AccountRekuestImpl.class);

			AccountResult result = accountService.addAccount(account);
			// todo:log

			return gson.toJson(result);
		});

		router.get("/account", (request, response) -> {
			response.type("application/json");

			return gson.toJson(
					new StandardResponse(StatusResponse.SUCCESS, gson.toJsonTree(accountService.getAccounts())));
		});

		router.get("/account/"+ ACC_ID_PATH_PARAM, (request, response) -> {
			response.type("application/json");

			AccountResult account = accountService.getAccount(request.params(":id"));
			if (account != null)
				return gson.toJson(account);
			else {
				return gson.toJson(account);
			}
		});

		router.put("/account/"+ACC_ID_PATH_PARAM, (request, response) -> {
			response.type("application/json");

			AccountRekuestImpl toEdit = gson.fromJson(request.body(), AccountRekuestImpl.class);
			AccountResult editedUser = accountService.editAccount(toEdit);

			if (editedUser != null) {
				return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJsonTree(editedUser)));
			} else {
				return gson.toJson(
						new StandardResponse(StatusResponse.ERROR, gson.toJson("User not found or error in edit")));
			}
		});

		router.delete("/account/"+ACC_ID_PATH_PARAM, (request, response) -> {
			response.type("application/json");

			accountService.deleteAccount(request.params(":id"));
			return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Account deleted"));
		});

		router.options("/account/"+ACC_ID_PATH_PARAM, (request, response) -> {
			response.type("application/json");

			return gson.toJson(new StandardResponse(StatusResponse.SUCCESS,
					(accountService.accountExist(request.params(":id"))) ? " Account exists"
							: " Account does not exists"));
		});
	}

}
