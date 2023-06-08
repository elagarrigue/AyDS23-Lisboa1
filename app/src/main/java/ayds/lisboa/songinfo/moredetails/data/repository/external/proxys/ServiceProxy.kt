package ayds.lisboa.songinfo.moredetails.data.repository.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ServiceProxy {
    fun getCardFromService(cardName: String): Card
}