package ayds.lisboa.songinfo.moredetails.broker.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa1.lastfm.LASTFM_IMAGE
import ayds.lisboa1.lastfm.LastFMArtistData
import ayds.lisboa1.lastfm.LastFMService

internal class LastFMProxy(
    private val lastFMService: LastFMService
) : ServiceProxy {

    override fun getCardFromService(artist: String): Card? {
        val artistData = lastFMService.getArtistData(artist)
        return artistData?.let { mapLastFMArtistToCard(it) }
    }

    private fun mapLastFMArtistToCard(lastFMArtistData: LastFMArtistData): Card {
        return Card.CardData(
            artistName = lastFMArtistData.artistName,
            description = lastFMArtistData.artisInfo,
            infoURL = lastFMArtistData.artistUrl,
            source = Card.Source.LastFM,
            sourceLogoURL = LASTFM_IMAGE,
        )
    }
}

