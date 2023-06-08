package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData

interface CardRepository {
    fun getCardData(cardName: String) : List<CardData>
}
