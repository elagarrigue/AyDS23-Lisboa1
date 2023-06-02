package ayds.lisboa.songinfo.moredetails.data.repository

import ayds.lisboa.songinfo.moredetails.data.repository.external.CardBroker
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.data.repository.local.CardLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData

class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val cardBroker: CardBroker
) : CardRepository {

    override fun getCardData(cardName: String): List<CardData> {
        var cardList = cardLocalStorage.getCards(cardName)

        when {
            cardList.isNotEmpty() -> cardList.markCardsAsLocal()
            else -> {
                cardList = cardBroker.getCard(cardName)
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
                cardLocalStorage.saveCard(card)
            }
        }
    }
}


