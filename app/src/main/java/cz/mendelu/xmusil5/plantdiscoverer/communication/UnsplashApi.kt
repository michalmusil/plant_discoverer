package cz.mendelu.xmusil5.plantdiscoverer.communication

import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.ImagesRequestResult
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.UnsplashImage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


interface UnsplashApi {

    companion object{
        private var accessKey: String = "YdRRIZYajleXjGWSowHSiBd5Ho87HE3waYxax09amIc"
    }

    @Headers("Content-Type: application/json")
    @GET("search/photos")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Header("Authorization") authorization: String = "Client-ID ${accessKey}"
    ): Response<ImagesRequestResult>
}