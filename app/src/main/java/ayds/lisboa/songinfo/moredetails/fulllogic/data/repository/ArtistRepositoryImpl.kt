package ayds.lisboa.songinfo.moredetails.fulllogic.data

import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistData
import ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.external.LastFMService
import ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.local.ArtistLocalStorage

interface ArtistRepository {
    fun getArtistData(artistName: String): ArtistData
}

private const val NO_RESULTS = "No results"

class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val lastFMService: LastFMService
) : ArtistRepository {

    override fun getArtistData(artistName: String): ArtistData {
        var artistData = getArtistFromDatabase(artistName)

        if (artistData.infoArtist != null) {
            artistData.markArtistAsLocal()
        }
        else {
            artistData = lastFMService.getArtistInfo(artistName)

            (artistData as ArtistData).let {
                if (it.infoArtist != NO_RESULTS)
                    artistLocalStorage.saveArtist(artistName, it.infoArtist)

            }
        }
        return artistData
    }

    private fun getArtistFromDatabase(artistName: String?): ArtistData {
        val infoArtist = artistLocalStorage.getInfo(artistName)
        return ArtistData(artistName, infoArtist)
    }

    private fun ArtistData.markArtistAsLocal() {
        isLocallyStored = true
    }




}