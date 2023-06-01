package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.broker.ArtistBroker
import ayds.lisboa.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.EmptyCard
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class ArtistRepositoryTest {

    private val artistLocalStorage : ArtistLocalStorage = mockk(relaxUnitFun = true)
    private val broker : ArtistBroker = mockk(relaxUnitFun = true)

    private val artistRepository : ArtistRepository by lazy {
        ArtistRepositoryImpl(artistLocalStorage, broker)
    }

    @Test
    fun `given existing artist should return artist and mark it as local`() {
        val artistCards: MutableList<CardData> = ArrayList()
        every { artistLocalStorage.getArtist("artist") } returns artistCards

        val result = artistRepository.getArtistData("artist")

        assertEquals(artistCards, result)
        assertTrue(artistData.isLocallyStored)
    }

    @Test
    fun `given non existing artist should get the artist and save it`() {
        val artistData = ArtistData("artist", "info", "url", false)
        every { artistLocalStorage.getArtist("artist") } returns null
        every { lastFMService.getArtist("artist") } returns artistData

        val result = artistRepository.getArtistData("artist")

        assertEquals(artistData, result)
        assertFalse(artistData.isLocallyStored)
        verify { artistLocalStorage.saveArtist(artistData) }
    }

    @Test
    fun `given non existing artist should return empty artist`() {
        every { artistLocalStorage.getArtist("artist") } returns null
        every { lastFMService.getArtist("artist") } returns null

        val result = artistRepository.getArtistData("artist")

        assertEquals(EmptyArtist, result)
    }

    @Test
    fun `given service exception should return empty artist`() {
        every { artistLocalStorage.getArtist("artist") } returns null
        every { lastFMService.getArtist("artist") } throws mockk<Exception>()

        val result = artistRepository.getArtistData("artist")

        assertEquals(EmptyArtist, result)
    }
}