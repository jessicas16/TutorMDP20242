package com.example.tutorm7front

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface HeroService {
    @GET("heroes")
    suspend fun getHeroes(): HeroResponse

//    Pake HeroResponse dan HeroDetailResponse karena kita harus sesuaiin
//    Dengan format return json dari backend
    @GET("heroes/{id}")
    suspend fun getHeroById(@Path("id") id: String): HeroDetailResponse

    @PUT("heroes/update/{id}")
    suspend fun updateHero(@Path("id") id: String, @Body hero: HeroEntity)

    @DELETE("heroes/{id}")
    suspend fun deleteHero(@Path("id") id: String)

}
