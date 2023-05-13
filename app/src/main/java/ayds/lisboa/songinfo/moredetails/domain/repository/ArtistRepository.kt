package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist


interface ArtistRepository {
    fun getArtistData(artistName: String) : Artist
}
