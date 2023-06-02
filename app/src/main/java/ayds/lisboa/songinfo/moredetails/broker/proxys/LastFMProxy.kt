package ayds.lisboa.songinfo.moredetails.broker.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.EmptyCard
import ayds.lisboa1.lastfm.LASTFM_IMAGE
import ayds.lisboa1.lastfm.LastFMArtistData
import ayds.lisboa1.lastfm.LastFMService

internal class LastFMProxy(
    private val lastFMService: LastFMService
) : ServiceProxy {

    override fun getCardFromService(cardName: String): Card {
        val card =
        try {
            val artistData = (lastFMService.getArtistData(cardName) as LastFMArtistData)
            mapLastFMArtistToCard(artistData)
        }
        catch(e: Exception) {
            EmptyCard
        }
        return card
    }

    private fun mapLastFMArtistToCard(lastFMArtistData: LastFMArtistData): Card {
        return CardData(
            cardName = lastFMArtistData.artistName,
            description = lastFMArtistData.artisInfo,
            infoURL = lastFMArtistData.artistUrl,
            source = Card.Source.LastFM,
            sourceLogoURL = LASTFM_IMAGE,
        )
    }
}

