

package home.test.restapi.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class GoogleJsonSerializer implements JsonSerializer{

	private final Gson json;
	public GoogleJsonSerializer(Gson json) {
		this.json =json;
	}
	
	@Override
	public <T> T fromJson(String json, Class<T> classOfT) {
		
		return this.json.fromJson(json, classOfT);
	}

	@Override
	public String toJson(Object src) {
		return this.json.toJson(src);
	}

	@Override
	public JsonElement toJsonTree(Object src) {
		return this.json.toJsonTree(src);
	}

	@Override
	public JsonElement toJsonTree(Object src, Type typeOfSrc) {
		return this.json.toJsonTree(src,typeOfSrc);
	}

}
