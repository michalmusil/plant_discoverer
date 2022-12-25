package cz.mendelu.xmusil5.plantdiscoverer.communication.repositories

import cz.mendelu.xmusil5.plantdiscoverer.communication.CommunicationResult
import cz.mendelu.xmusil5.plantdiscoverer.communication.IBaseRemoteRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.ImagesRequestResult
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.UnsplashImage

interface IUnsplashImagesRepository: IBaseRemoteRepository {

    suspend fun fetchImages(query: String): CommunicationResult<ImagesRequestResult>
}