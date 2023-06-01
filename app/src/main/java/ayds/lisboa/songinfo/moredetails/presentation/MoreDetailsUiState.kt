package ayds.lisboa.songinfo.moredetails.presentation

data class MoreDetailsUiState(
    var artistCards: List<CardDataState>, var selectedIndex: Int = 0
)

data class CardDataState(
    val artistName: String = "",
    val description: String = "",
    val infoURL: String = "",
    val source: String = "",
    val sourceLogo: String = ""
)