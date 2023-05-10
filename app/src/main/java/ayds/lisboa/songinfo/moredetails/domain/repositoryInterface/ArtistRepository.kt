package ayds.lisboa.songinfo.moredetails.domain.repositoryInterface

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist


interface ArtistRepository {
    fun getArtistData(artistName: String) : Artist
}
