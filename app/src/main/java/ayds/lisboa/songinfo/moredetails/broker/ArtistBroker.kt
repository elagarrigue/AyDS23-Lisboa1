package ayds.lisboa.songinfo.moredetails.broker

import ayds.lisboa.songinfo.moredetails.data.repository.ServiceProxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ArtistBroker {
    fun getCard(artist: String): List<Card?>
}

internal class ArtistBrokerImpl(
    private val proxyList: List<ServiceProxy>
) : ArtistBroker {

    override fun getCard(artist: String): List<Card?> {
        val artistCards: MutableList<Card?> = ArrayList()
        for (proxyService in proxyList) {
            val card = proxyService.getCardFromService(artist)
            artistCards.add(card)
        }
        return artistCards
    }

}
