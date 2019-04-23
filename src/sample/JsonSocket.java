package sample;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


public class JsonSocket {

    private JsonArray array;
    private Gson gson;


    public JsonSocket() {
        this.array= new JsonArray();
        gson= new Gson();
    }

    public void appendJson(Movement movement) {
        JsonElement element=gson.toJsonTree(movement, Movement.class);
        //array.add(gson.toJson(movement));
        array.add(element);
    }

    public String getJson(){
        return gson.toJson(array);
    }

    public String cleanAndGet(){
        String s=getJson();
        array= new JsonArray();
        return s;
    }

    public Movement[] getMovements(String json){
        return gson.fromJson(json, Movement[].class);
    }

}
