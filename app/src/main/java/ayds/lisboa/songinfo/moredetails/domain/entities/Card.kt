package ayds.lisboa.songinfo.moredetails.domain.entities

sealed class Card {
    data class CardData(
        var artistName: String,
        var description: String,
        var infoURL: String = "",
        var source: String = "https://github.com/Federico-manolo/Lisbo1-LastFMData.git",
        var sourceLogoURL: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png",
        var isLocallyStored: Boolean = false
    ) : Card()

    object EmptyCard : Card()
}