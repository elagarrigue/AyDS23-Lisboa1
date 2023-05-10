package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repositoryInterface.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Subject
import java.util.*

private const val HTML_START = "<html><div width=400>"
private const val HTML_END = "</font></div></html>"
private const val FONT_FACE = "<font face=\"arial\">"

interface MoreDetailsPresenter {
    val artistObservable: Observable<Artist>
    fun getArtistMoreInformation(artistName: String)
}

internal class MoreDetailsPresenterImpl(private val repository: ArtistRepository) :
    MoreDetailsPresenter {

    override val artistObservable = Subject<Artist>()

    override fun getArtistMoreInformation(artistName: String) {
        val artistData: Artist = repository.getArtistData(artistName)
        artistData.getFormattingInfoArtist(artistName)
        artistObservable.notify(artistData)
    }

    private fun Artist.getFormattingInfoArtist(artistName: String) {
        val infoToFormat = (this as ArtistData).infoArtist
        this.infoArtist = textToHtml(infoToFormat, artistName)
    }

    private fun textToHtml(text: String, term: String): String {
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
}


