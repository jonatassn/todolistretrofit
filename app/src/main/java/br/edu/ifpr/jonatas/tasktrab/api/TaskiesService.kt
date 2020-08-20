package br.edu.ifpr.jonatas.tasktrab.api

import br.edu.ifpr.jonatas.tasktrab.entities.Tasky
import retrofit2.Call
import retrofit2.http.*

interface TaskiesService {
    @GET("tasks")
    fun getAll(): Call<List<Tasky>>

    @GET("tasks/{id}")
    fun get(@Path("id") id: Long): Call<Tasky>

    @Headers("Content-Type: Application/json")
    @POST("tasks")
    fun insert(@Body task : Tasky) : Call<Tasky>

    @Headers("Content-Type: Application/json")
    @PATCH("tasks/{id}")
    fun update(@Path("id") id: Long, @Body task : Tasky) : Call<Tasky>

    @DELETE("tasks/{id}")
    fun delete(@Path("id") id: Long) : Call<Void>
}