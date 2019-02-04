package com.fffrowies.sbadmin.Remote;

import com.fffrowies.sbadmin.Model.MyResponse;
import com.fffrowies.sbadmin.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAfsmac_U:APA91bHkg7eYeGy4xSYvAw_4LyWqfFjBxoU7Vr450Bl9oEWOj3Ie96BoAJocKkrQ1tzgEMOlwq3j07jcn7LKnRk0aWKZd7-l9yIcxOV3oEo2I22tuWWEliP8KlGreW4NEk4NIZTxI_rn"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
