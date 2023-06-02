package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.repository.CardRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.repository.external.LastFMService
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.EmptyArtist
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class CardRepositoryTest {

    private val artistLocalStorage : ArtistLocalStorage = mockk(relaxUnitFun = true)
    private val lastFMService : LastFMService = mockk(relaxUnitFun = true)

    private val cardRepository : CardRepository by lazy {
        CardRepositoryImpl(artistLocalStorage, lastFMService)
    }

    @Test
    fun `given existing artist should return artist and mark it as local`() {
        val artistData = ArtistData("artist", "info", "url", false)
        every { artistLocalStorage.getArtist("artist") } returns artistData

        val result = cardRepository.getArtistData("artist")

        assertEquals(artistData, result)
        assertTrue(artistData.isLocallyStored)
    }

    @Test
    fun `given non existing artist should get the artist and save it`() {
        val artistData = ArtistData("artist", "info", "url", false)
        every { artistLocalStorage.getArtist("artist") } returns null
        every { lastFMService.getArtist("artist") } returns artistData

        val result = cardRepository.getArtistData("artist")

        assertEquals(artistData, result)
        assertFalse(artistData.isLocallyStored)
        verify { artistLocalStorage.saveArtist(artistData) }
    }

    @Test
    fun `given non existing artist should return empty artist`() {
        every { artistLocalStorage.getArtist("artist") } returns null
        every { lastFMService.getArtist("artist") } returns null

        val result = cardRepository.getArtistData("artist")

        assertEquals(EmptyArtist, result)
    }

    @Test
    fun `given service exception should return empty artist`() {
        every { artistLocalStorage.getArtist("artist") } returns null
        every { lastFMService.getArtist("artist") } throws mockk<Exception>()

        val result = cardRepository.getArtistData("artist")

        assertEquals(EmptyArtist, result)
    }
}