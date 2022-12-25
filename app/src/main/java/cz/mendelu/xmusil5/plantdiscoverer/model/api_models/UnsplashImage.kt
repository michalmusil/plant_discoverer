package cz.mendelu.xmusil5.plantdiscoverer.model.api_models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashImage(
    @Json(name="id")val id: String,
    @Json(name = "urls")val imageUrls: ImageUrls,
)
