
package home.test.restapi.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;

public interface JsonSerializer {
	<T> T fromJson(String json, Class<T> classOfT);

	String toJson(Object src);

	JsonElement toJsonTree(Object src);

	JsonElement toJsonTree(Object src, Type typeOfSrc);
}
