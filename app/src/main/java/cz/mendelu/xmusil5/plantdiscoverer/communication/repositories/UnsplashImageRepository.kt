package cz.mendelu.xmusil5.plantdiscoverer.communication.repositories

import cz.mendelu.xmusil5.plantdiscoverer.communication.CommunicationResult
import cz.mendelu.xmusil5.plantdiscoverer.communication.UnsplashApi
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.ImagesRequestResult
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.UnsplashImage

class UnsplashImageRepository(private val unsplashApi: UnsplashApi): IUnsplashImagesRepository {

    override suspend fun fetchImages(query: String): CommunicationResult<ImagesRequestResult> {
        return processResponse(unsplashApi.searchImages(query))
    }
}