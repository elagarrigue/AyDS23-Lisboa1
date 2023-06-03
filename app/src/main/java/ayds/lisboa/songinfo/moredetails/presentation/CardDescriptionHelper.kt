package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.Source
import java.util.*

private const val HTML_START = "<html><div width=400>"
private const val HTML_END = "</font></div></html>"
private const val FONT_FACE = "<font face=\"arial\">"

private const val SOURCE_STRING = "Fuente: "
interface CardDescriptionHelper {
    fun textToHtml(text: String, term: String): String

    fun formatSource(source: Source): String
}

class CardDescriptionHelperImpl : CardDescriptionHelper {

    override fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append(HTML_START)
        builder.append(FONT_FACE)
        val textWithBold = text.replace("'", " ").replace("\n", "<br>").replace(
            "(?i)$term".toRegex(), "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
        )
        builder.append(textWithBold)
        builder.append(HTML_END)
        return builder.toString()
    }

    override fun formatSource(source: Source): String {
        return SOURCE_STRING +
                when(source) {
            Source.LastFM -> "LastFM"
            Source.NYTimes -> "New York Times"
            Source.Wikipedia -> "Wikipedia"
        }
    }

}