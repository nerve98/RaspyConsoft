package sample;

import com.google.gson.Gson;

public class Movement {
    private String badge;
    private String data_time;

    public Movement(String badge, String data1) {
        this.badge = badge;
        this.data_time = data1;

    }

    public String getBadge() {
        return badge;
    }

    public String getDataTime() {
        return data_time;
    }


}
