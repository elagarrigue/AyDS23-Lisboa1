package ayds.lisboa.songinfo.moredetails.broker

import ayds.lisboa.songinfo.moredetails.broker.proxys.ServiceProxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ArtistBroker {
    fun getCard(artist: String): List<CardData>
}

internal class ArtistBrokerImpl(
    private val proxyList: List<ServiceProxy>
) : ArtistBroker {

    override fun getCard(artist: String): List<CardData> {
        val artistCards: MutableList<Card> = ArrayList()
        for (proxyService in proxyList) {
            val card = proxyService.getCardFromService(artist)
            artistCards.add(card)
        }
        return artistCards.filterIsInstance<CardData>()
    }

}
