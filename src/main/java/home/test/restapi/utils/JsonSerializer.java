
package home.test.restapi.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;

/**
 * Need to add more methods as as when rekuired
 * @author sachin
 *
 * Type JsonSerializer, created on 25-Sep-2019 at 8:01:50 pm
 *
 */
public interface JsonSerializer {
	<T> T fromJson(String json, Class<T> classOfT);

	String toJson(Object src);

	JsonElement toJsonTree(Object src);

	JsonElement toJsonTree(Object src, Type typeOfSrc);
}
