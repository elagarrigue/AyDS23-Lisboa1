package ayds.lisboa.songinfo.moredetails.presentation

data class MoreDetailsUiState(
    var cardsDataState: List<CardDataState>, var selectedIndex: Int = 0
)

data class CardDataState(
    val cardName: String = "",
    val description: String = "",
    val infoURL: String = "",
    val sourceName: String = "",
    val sourceLogo: String = ""
)