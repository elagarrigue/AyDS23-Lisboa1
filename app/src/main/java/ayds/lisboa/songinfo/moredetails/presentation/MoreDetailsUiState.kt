package ayds.lisboa.songinfo.moredetails.presentation
data class MoreDetailsUiState (
    val artistName: String = "",
    var infoArtist: String = "",
    val url: String = "",
    val isLocallyStored: Boolean = false,
    val imageUrl: String = DEFAULT_IMAGE
){
    companion object {
        const val DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }

}