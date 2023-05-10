package ayds.lisboa.songinfo.moredetails.fulllogic.domain.repositoryInterface

import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistData

interface ArtistRepository {
    fun getArtistData(artistName: String) : ArtistData
}
