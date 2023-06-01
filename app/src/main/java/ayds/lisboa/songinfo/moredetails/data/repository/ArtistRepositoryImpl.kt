package ayds.lisboa.songinfo.moredetails.data.repository

import ayds.lisboa.songinfo.moredetails.broker.ArtistBroker
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData

class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val broker: ArtistBroker
) : ArtistRepository {

    override fun getArtistData(artistName: String): List<CardData> {
        var cardList = artistLocalStorage.getArtist(artistName)

        when {
            cardList.isNotEmpty() -> {
                    cardList.markCardsAsLocal()
            }
            else -> {
                cardList = broker.getCard(artistName)
                cardList.saveCards()
            }
        }
        return cardList
    }

    private fun List<Card>.markCardsAsLocal() {
        for (card in this) {
            if(card is CardData) {
                card.isLocallyStored = true
            }
        }
    }

    private fun List<Card>.saveCards() {
        for (card in this) {
            if(card is CardData) {
                artistLocalStorage.saveArtist(card)
            }
        }
    }
}


