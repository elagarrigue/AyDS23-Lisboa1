package ayds.lisboa.songinfo.moredetails.data.repository

import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa1.lastfm.LastFMService
import ayds.lisboa1.lastfm.LastFMArtistData
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.EmptyArtist

class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val lastFMService: LastFMService
) : ArtistRepository {

    override fun getArtistData(artistName: String): Artist {
        var artistData = artistLocalStorage.getArtist(artistName)

        when {
            artistData != null -> artistData.markArtistAsLocal()
            else -> {
                try {
                    var lastFMArtistData = lastFMService.getArtistData(artistName)
                    artistData = adaptLastFMArtistData(lastFMArtistData)
                    artistData?.let {
                        artistLocalStorage.saveArtist(it)
                    }
                } catch (e: Exception) {
                    artistData = null
                }
            }
        }
        return artistData ?: EmptyArtist
    }

    private fun adaptLastFMArtistData(lastFMArtistData: LastFMArtistData?): ArtistData? =
        lastFMArtistData?.let {ArtistData(lastFMArtistData.artistName,lastFMArtistData.artisInfo,lastFMArtistData.artistUrl)}


    private fun ArtistData.markArtistAsLocal() {
        isLocallyStored = true
    }
}


