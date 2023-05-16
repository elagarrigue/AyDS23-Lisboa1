package ayds.lisboa.songinfo.moredetails.data.repository.external

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import retrofit2.Response

interface LastFMService {

    fun getArtist(artistName : String): ArtistData?

}

internal class LastFMServiceImpl(
    private val lastFMAPI : LastFMAPI,
    private val lastFMToArtistDataResolver: LastFMToArtistDataResolver
) : LastFMService {

    override fun getArtist(artistName: String): ArtistData? {
        val callResponse = getArtistFromAPI(artistName)
        return lastFMToArtistDataResolver.getArtistFromExternalData(callResponse.body())
    }

    private fun getArtistFromAPI(query: String): Response<String> {
        return lastFMAPI.getArtistInfo(query).execute()
    }

}