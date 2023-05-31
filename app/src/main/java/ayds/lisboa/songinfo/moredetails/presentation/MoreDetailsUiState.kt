package ayds.lisboa.songinfo.moredetails.presentation
data class MoreDetailsUiState(
    val artistCards: List<CardDataState>
)

data class CardDataState(
    val artistName: String = "",
    val description: String = "",
    val infoURL: String = "",
    val source: String = "",
    val sourceLogo: String = "",
)