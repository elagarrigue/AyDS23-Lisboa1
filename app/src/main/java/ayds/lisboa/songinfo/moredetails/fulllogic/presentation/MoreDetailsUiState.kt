package ayds.lisboa.songinfo.moredetails.fulllogic.presentation
class MoreDetailsUiState (
    val artistName: String = "",
    val artistUrl: String = "",
    val artistDescription: String = "",
    val imageUrl: String = DEFAULT_IMAGE
){
    companion object {
        const val DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }

}