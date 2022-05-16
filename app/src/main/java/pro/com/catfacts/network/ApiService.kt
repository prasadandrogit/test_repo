package pro.com.catfacts.network

import pro.com.catfacts.model.CatRandomFact
import retrofit2.http.GET

interface ApiService {

    @GET("facts/random")
    suspend fun getRandomFact(): CatRandomFact
}