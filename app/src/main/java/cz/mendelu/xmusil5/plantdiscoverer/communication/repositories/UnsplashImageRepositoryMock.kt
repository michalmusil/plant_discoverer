package cz.mendelu.xmusil5.plantdiscoverer.communication.repositories

import cz.mendelu.xmusil5.plantdiscoverer.communication.CommunicationResult
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.ImageUrls
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.ImagesRequestResult
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.UnsplashImage

class UnsplashImageRepositoryMock: IUnsplashImagesRepository {

    companion object{
        val imagesMock = listOf(
            UnsplashImage("1", ImageUrls(small = "https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg",
                regular = "https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg",
                full = "https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg")),
            UnsplashImage("2", ImageUrls(small = "https://www.gardendesign.com/pictures/images/675x529Max/site_3/helianthus-yellow-flower-pixabay_11863.jpg",
                regular = "https://www.gardendesign.com/pictures/images/675x529Max/site_3/helianthus-yellow-flower-pixabay_11863.jpg",
                full = "https://www.gardendesign.com/pictures/images/675x529Max/site_3/helianthus-yellow-flower-pixabay_11863.jpg")),
            UnsplashImage("3", ImageUrls(small = "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg",
                regular = "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg",
                full = "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg"))
            )
    }

    override suspend fun fetchImages(
        query: String,
        page: Int
    ): CommunicationResult<ImagesRequestResult> {

        val requestResult = ImagesRequestResult(UnsplashImageRepositoryMock.imagesMock)
        return CommunicationResult.Success(data = requestResult)
    }
}