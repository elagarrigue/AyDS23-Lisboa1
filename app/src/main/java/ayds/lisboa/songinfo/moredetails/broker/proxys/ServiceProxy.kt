package ayds.lisboa.songinfo.moredetails.broker.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ServiceProxy {
    fun getCardFromService(cardName: String): Card
}