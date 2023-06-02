package ayds.lisboa.songinfo.moredetails.broker

import ayds.lisboa.songinfo.moredetails.broker.proxys.ServiceProxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CardBroker {
    fun getCard(cardName: String): List<CardData>
}

internal class CardBrokerImpl(
    private val proxyList: List<ServiceProxy>
) : CardBroker {

    override fun getCard(cardName: String): List<CardData> {
        val cards: MutableList<Card> = ArrayList()
        for (proxyService in proxyList) {
            val card = proxyService.getCardFromService(cardName)
            cards.add(card)
        }
        return cards.filterIsInstance<CardData>()
    }

}
