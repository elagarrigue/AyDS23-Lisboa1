package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData

interface ArtistRepository {
    fun getArtistData(artistName: String) : List<CardData>
}
