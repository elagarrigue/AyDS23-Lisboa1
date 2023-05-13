package ayds.lisboa.songinfo.moredetails.data.repository

import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.data.repository.external.LastFMService
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.EmptyArtist

private const val NO_RESULTS = "No results"

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
                    artistData = lastFMService.getArtist(artistName)

                    (artistData as? ArtistData)?.let {
                        if (it.infoArtist != NO_RESULTS)
                            artistLocalStorage.saveArtist(it)

                    }
                } catch (e: Exception) {
                    artistData = null
                }
            }
        }
        return artistData ?: EmptyArtist
    }

    private fun ArtistData.markArtistAsLocal() {
        isLocallyStored = true
    }
}


