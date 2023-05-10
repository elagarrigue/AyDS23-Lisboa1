package ayds.lisboa.songinfo.moredetails.data.repository.external

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import com.google.gson.Gson
import com.google.gson.JsonObject

const val ARTIST_CONST = "artist"
const val BIO_ARTIST_CONST = "bio"
const val CONTENT_ARTIST_CONST = "content"
const val URL_ARTIST_CONST = "url"
const val NO_RESULTS = "No results"

interface LastFMToArtistDataResolver {
    fun getArtistFromExternalData(artistName: String?): ArtistData?
}

internal class JSONToArtistDataResolver : LastFMToArtistDataResolver {

    override fun getArtistFromExternalData(artistName: String?): ArtistData? =
        try {
            artistName?.let {
                val jObjectArtist = artistResponseToJson(artistName)
                ArtistData(artistName, jObjectArtist.getFormattingDataArtist(), jObjectArtist.getArtistURL()) }
        } catch (e: Exception) {
            null
        }

    private fun artistResponseToJson(string: String): JsonObject {
        return Gson().fromJson(string, JsonObject::class.java)
    }

    private fun JsonObject.getFormattingDataArtist(): String {
        val dataArtistString = getArtistBioContentToString()
        return dataArtistString ?: NO_RESULTS
    }

    private fun JsonObject.getArtistBioContentToString(): String? {
        val artistObj = this[ARTIST_CONST].asJsonObject
        val bioObj = artistObj[BIO_ARTIST_CONST].asJsonObject
        val contentArtist = bioObj[CONTENT_ARTIST_CONST]
        return contentArtist.asString.replace("\\n", "\n")
    }

    private fun JsonObject.getArtistURL(): String {
        val artistObj = this[ARTIST_CONST].asJsonObject
        return artistObj[URL_ARTIST_CONST].asString
    }
}