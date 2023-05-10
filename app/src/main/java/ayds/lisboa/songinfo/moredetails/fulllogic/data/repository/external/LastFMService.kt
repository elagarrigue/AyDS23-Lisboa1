package ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.external

import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistData
import retrofit2.Response

interface LastFMService {

    fun getArtistInfo(artistName : String): ArtistData

}

internal class LastFMServiceImpl(
    private val lastFMAPI : LastFMAPI,
    private val lastFMToArtistDataResolver: LastFMToArtistDataResolver
) : LastFMService {

    override fun getArtistInfo(artistName: String): ArtistData {
        val callResponse = getArtistInfoFromAPI(artistName)
        return lastFMToArtistDataResolver.getArtistFromExternalData(callResponse.body())
    }

    private fun getArtistInfoFromAPI(query: String): Response<String> {
        return lastFMAPI.getArtistInfo(query).execute()
    }

}