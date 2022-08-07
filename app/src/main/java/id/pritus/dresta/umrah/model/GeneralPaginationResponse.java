package id.pritus.dresta.umrah.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import id.pritus.dresta.umrah.home.BerandaFragment;

public class GeneralPaginationResponse {
    @SerializedName("status")
    Integer status;
    @SerializedName("msg")
    String msg;
    @SerializedName("page")
    Integer page;
    @SerializedName("totalPage")
    int totalPage;
    @SerializedName("recordsFiltered")
    Integer recordsFiltered;
    @SerializedName("totalRecords")
    Integer totalRecords;
    @SerializedName("data")
    List<Object> data;

    public GeneralPaginationResponse(Integer status, String msg, Integer page, int totalPage, Integer recordsFiltered, Integer totalRecords, List<Object> data) {
        this.status = status;
        this.msg = msg;
        this.page = page;
        this.totalPage = totalPage;
        this.recordsFiltered = recordsFiltered;
        this.totalRecords = totalRecords;
        this.data = data;
    }

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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

//    public <T> List<T> getData(Class<T> type) {
//        Gson gson = new Gson();
//        return (List<T>) gson.fromJson(gson.toJson(data), type);
//    }

//List<Slider> sliders = gson.fromJson(gson.toJson(response.body().getData()), new TypeToken<List<Slider>>(){}.getType());

    public <T> List<T> getData(Class<T> clazz ) {
        Gson gson = new Gson();
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return new Gson().fromJson(gson.toJson(data), typeOfT);
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
