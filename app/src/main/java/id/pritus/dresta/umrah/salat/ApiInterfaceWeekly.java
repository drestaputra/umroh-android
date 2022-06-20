package id.pritus.dresta.umrah.salat;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaceWeekly {

    @GET("{periode}/weekly.json")
    Call<Items> getJadwalSholat(@Path("periode") String periode);
}
