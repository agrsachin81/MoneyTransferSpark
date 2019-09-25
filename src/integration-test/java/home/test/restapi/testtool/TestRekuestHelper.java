
package home.test.restapi.testtool;

import static org.junit.Assert.fail;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import spark.utils.IOUtils;

public class TestRekuestHelper {
	public static TestResponse request(String method, String path, String json) {
		try {
			URL url = new URL("http://localhost:1010" + path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("charset", "utf-8");
				byte[] postData = json.getBytes(StandardCharsets.UTF_8);
				connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
				//connection.connect();
				try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
					wr.write(postData);
				}
			} else {
				connection.connect();
			}
			String body = IOUtils.toString(connection.getInputStream());
			return new TestResponse(connection.getResponseCode(), body);
		} catch (

		IOException e) {
			e.printStackTrace();
			fail("Sending request failed: " + e.getMessage());
			return null;
		}
	}
}
