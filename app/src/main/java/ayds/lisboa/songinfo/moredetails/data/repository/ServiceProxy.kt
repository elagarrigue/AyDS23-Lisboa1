package ayds.lisboa.songinfo.moredetails.data.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ServiceProxy {
    fun getCardFromService(artist: String): Card?
}