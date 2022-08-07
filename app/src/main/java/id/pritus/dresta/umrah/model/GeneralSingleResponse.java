package id.pritus.dresta.umrah.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GeneralSingleResponse {
    @SerializedName("status")
    Integer status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public <T> T getData(Class<T> type) {
        Gson gson = new Gson();
        return (T) gson.fromJson(gson.toJson(data), type);
    }
}
