package org.d3if3115.mobpro1.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3115.catalogkendaraan.model.Kendaraan
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/" +
        "indraazimi/galeri-hewan/static-api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KendaraanApiService {
    @GET("static-api.json")
    suspend fun getKendaraan(): List<Kendaraan>
}

object KendaraanApi {
    val service: KendaraanApiService by lazy {
        retrofit.create(KendaraanApiService::class.java)
    }
    fun getKendaraanUrl(imageId: String): String {
        return "$BASE_URL$imageId"
    }
}
enum class ApiStatus { LOADING, SUCCESS, FAILED }