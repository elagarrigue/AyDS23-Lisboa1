package ayds.lisboa.songinfo.moredetails.domain.entities

private const val LASTFMSOURCE = "LastFM"
private const val NYTIMESSOURCE = "New York Times"
private const val WIKIPEDIASOURCE = "Wikipedia"

sealed class Card {
    data class CardData(
        var artistName: String,
        var description: String,
        var infoURL: String = "",
        var source: Source,
        var sourceLogoURL: String = "",
        var isLocallyStored: Boolean = false
    ) : Card()

    object EmptyCard : Card()

    enum class Source {
        LASTFMSOURCE,
        NYTIMESSOURCE,
        WIKIPEDIASOURCE,
    }

}