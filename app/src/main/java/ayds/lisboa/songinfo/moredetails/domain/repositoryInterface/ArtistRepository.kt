package ayds.lisboa.songinfo.moredetails.domain.repositoryInterface

import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistData


interface ArtistRepository {
    fun getArtistData(artistName: String) : ArtistData
}
