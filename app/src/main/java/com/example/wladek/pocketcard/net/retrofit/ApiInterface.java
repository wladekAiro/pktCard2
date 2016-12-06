package com.example.wladek.pocketcard.net.retrofit;

import com.example.wladek.pocketcard.pojo.CheckOutData;
import com.example.wladek.pocketcard.pojo.CheckOutResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by wladek on 10/23/16.
 */
public interface ApiInterface {
    @POST("student_checkout")
    Call<CheckOutResponse> checkOutStudent(@Body CheckOutData checkOut);

//    @GET("movie/top_rated")
//    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);
//
//    @GET("movie/{id}")
//    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
