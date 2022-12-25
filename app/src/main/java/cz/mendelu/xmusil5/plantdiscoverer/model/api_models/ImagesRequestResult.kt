package cz.mendelu.xmusil5.plantdiscoverer.model.api_models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesRequestResult(
    @Json(name = "results") val results: List<UnsplashImage>
)
