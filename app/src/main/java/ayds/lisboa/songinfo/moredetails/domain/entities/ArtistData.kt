package ayds.lisboa.songinfo.moredetails.domain.entities

sealed class Artist {
    data class ArtistData(
        var artistName: String,
        var infoArtist: String,
        var url: String = "",
        var isLocallyStored: Boolean = false
    ) : Artist()

    object EmptyArtist : Artist()
}