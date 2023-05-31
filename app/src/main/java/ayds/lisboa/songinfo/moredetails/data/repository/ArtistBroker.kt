package ayds.lisboa.songinfo.moredetails.data.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ArtistBroker {
    fun getCard(artist: String): MutableList<Card?>
}

internal class ArtistBrokerImpl(
    private val proxyList: MutableList<ServiceProxy>
): ArtistBroker {

    override fun getCard(artist: String): MutableList<Card?> {
        val artistCards: MutableList<Card?> = ArrayList()
        for (proxyService in proxyList) {
            val card = proxyService.getCardFromService(artist)
            artistCards.add(card)
        }
        return artistCards
    }
}
