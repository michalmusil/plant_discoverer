package cz.mendelu.xmusil5.plantdiscoverer

import cz.mendelu.xmusil5.plantdiscoverer.communication.CommunicationResult
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.IUnsplashImagesRepository
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.UnsplashImageRepositoryMock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class UnsplashApiRepoTest {
    val unsplashImageRepo: IUnsplashImagesRepository = UnsplashImageRepositoryMock()

    @Test
    fun imagesFetchedAndMatching(){
        runBlocking {
            val images = unsplashImageRepo.fetchImages("", 1)
            assert(images is CommunicationResult.Success)
            val imagesSuccess = images as CommunicationResult.Success
            assertEquals(imagesSuccess.data.results.size, UnsplashImageRepositoryMock.imagesMock.size)
            assertEquals(imagesSuccess.data.results.first(), UnsplashImageRepositoryMock.imagesMock.first())
        }
    }

}