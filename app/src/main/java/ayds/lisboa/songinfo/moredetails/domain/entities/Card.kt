package ayds.lisboa.songinfo.moredetails.domain.entities

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
        LastFM,
        NYTimes,
        Wikipedia
    }

}