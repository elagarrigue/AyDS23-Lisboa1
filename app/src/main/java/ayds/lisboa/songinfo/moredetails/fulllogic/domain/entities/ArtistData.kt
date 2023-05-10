package ayds.lisboa.songinfo.moredetails.fulllogic.domain.entities

data class ArtistData(
    var artistName: String?,
    var infoArtist: String?,
    var url: String = "",
    var isLocallyStored: Boolean = false
)