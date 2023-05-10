package ayds.lisboa.songinfo.moredetails.fulllogic.data.repository.external

import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistData
import ayds.lisboa.songinfo.moredetails.fulllogic.OtherInfoWindow
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.*

const val ARTIST_CONST = "artist"
const val BIO_ARTIST_CONST = "bio"
const val CONTENT_ARTIST_CONST = "content"
const val URL_ARTIST_CONST = "url"
const val NO_RESULTS = "No results"

interface LastFMToArtistDataResolver {
    fun getArtistFromExternalData(artistName: String?): ArtistData
}

internal class JSONToArtistDataResolver : LastFMToArtistDataResolver {

    override fun getArtistFromExternalData(artistName: String?): ArtistData {
        val jObjectArtist = artistResponseToJson(artistName)
        return ArtistData(artistName, jObjectArtist.getFormattingDataArtist(artistName), jObjectArtist.getArtistURL())
    }

    private fun artistResponseToJson(string: String?): JsonObject {
        return Gson().fromJson(string, JsonObject::class.java)
    }

    private fun JsonObject.getFormattingDataArtist(artistName: String?): String {
        var formattedInfoArtist: String? = null
        val contentArtist = getArtistBioContent()
        if (contentArtist != null) {
            val dataArtistString = contentArtist.asString.replace("\\n", "\n")
            formattedInfoArtist = artistName?.let { textToHtml(dataArtistString, it) }
        }
        return formattedInfoArtist ?: NO_RESULTS
    }

    private fun JsonObject.getArtistBioContent(): JsonElement? {
        val artistObj = this[ARTIST_CONST].asJsonObject
        val bioObj = artistObj[BIO_ARTIST_CONST].asJsonObject
        return bioObj[CONTENT_ARTIST_CONST]
    }

    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append(OtherInfoWindow.HTML_START)
        builder.append(OtherInfoWindow.FONT_FACE)
        val textWithBold = text.replace("'", " ").replace("\n", "<br>").replace(
            "(?i)$term".toRegex(), "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
        )
        builder.append(textWithBold)
        builder.append(OtherInfoWindow.HTML_END)
        return builder.toString()
    }

    private fun JsonObject.getArtistURL(): String {
        val artistObj = this[ARTIST_CONST].asJsonObject
        return artistObj[URL_ARTIST_CONST].asString
    }
}