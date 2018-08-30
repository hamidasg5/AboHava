package com.abohava.data.api;

import com.abohava.data.api.model.ApiBaseWeather;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

public class WeatherDeserializer<W extends ApiBaseWeather> implements JsonDeserializer<List<W>> {

    @Override
    public List<W> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Gson().fromJson(json.getAsJsonObject().get("data"), typeOfT);
    }
}