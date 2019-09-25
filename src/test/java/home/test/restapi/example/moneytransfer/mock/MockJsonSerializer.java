

package home.test.restapi.example.moneytransfer.mock;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import home.test.restapi.utils.GoogleJsonSerializer;

public class MockJsonSerializer extends GoogleJsonSerializer {

	public MockJsonSerializer(Gson json) {
		super(json);
	}

	private Object toJson =null;
	private Object jsonTreeObj;
	private Type type;
	private Object jsonTreeObjType;
	
	public Object getToJsonLastObject(){
		return toJson;
	}
	
	@Override
	public String toJson(Object src) {
		this.toJson =src;
		return "";
	}

	@Override
	public JsonElement toJsonTree(Object src) {
		this.jsonTreeObj = src;
		return super.toJsonTree(src);
	}

	@Override
	public JsonElement toJsonTree(Object src, Type typeOfSrc) {
		this.jsonTreeObjType = src;
		this.type = typeOfSrc;
		return super.toJsonTree(src, typeOfSrc);
	}

	public Object getJsonTreeObjLastObject() {
		return jsonTreeObj;
	}

	public Object getJsonTreeObjTypeLastObject() {
		return jsonTreeObjType;
	}

	public Type getTypeLastObject() {
		return type;
	}

}
