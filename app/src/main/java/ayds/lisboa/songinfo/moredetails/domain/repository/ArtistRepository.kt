package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ArtistRepository {
    fun getArtistData(artistName: String) : List<Card>
}
