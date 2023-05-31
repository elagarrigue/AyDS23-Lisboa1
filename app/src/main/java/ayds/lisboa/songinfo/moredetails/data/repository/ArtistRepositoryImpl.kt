package ayds.lisboa.songinfo.moredetails.data.repository

import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa1.lastfm.LastFMService
import ayds.lisboa1.lastfm.LastFMArtistData
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.EmptyCard

class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val lastFMService: LastFMService
) : ArtistRepository {

    override fun getArtistData(artistName: String): Card {
        var artistData = artistLocalStorage.getArtist(artistName)

        when {
            artistData != null -> artistData.markArtistAsLocal()
            else -> {
                try {
                    val lastFMArtistData = lastFMService.getArtistData(artistName)
                    artistData = adaptLastFMArtistData(lastFMArtistData)
                    artistData?.let {
                        artistLocalStorage.saveArtist(it)
                    }
                } catch (e: Exception) {
                    artistData = null
                }
            }
        }
        return artistData ?: EmptyCard
    }

    private fun adaptLastFMArtistData(lastFMArtistData: LastFMArtistData?): CardData? =
        lastFMArtistData?.let {CardData(lastFMArtistData.artistName,lastFMArtistData.artisInfo,lastFMArtistData.artistUrl)}


    private fun CardData.markArtistAsLocal() {
        isLocallyStored = true
    }
}


