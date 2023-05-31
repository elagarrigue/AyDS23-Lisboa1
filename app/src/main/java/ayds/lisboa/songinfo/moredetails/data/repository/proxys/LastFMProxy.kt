package ayds.lisboa.songinfo.moredetails.data.repository.proxys

import ayds.lisboa.songinfo.moredetails.data.repository.ServiceProxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa1.lastfm.LastFMArtistData
import ayds.lisboa1.lastfm.LastFMService

private const val LASTFMSOURCE = "LastFM"
private const val LASTFMLOGO = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

internal class LastFMProxy(
    private val lastFMService: LastFMService
): ServiceProxy {

    override fun getCardFromService(artist: String): Card? {
        val artistData = lastFMService.getArtistData(artist)
        return artistData?.let { mapLastFMArtistToCard(it) }
    }

    private fun mapLastFMArtistToCard(lastFMArtistData: LastFMArtistData): Card {
        return Card.CardData(
            artistName = lastFMArtistData.artistName,
            description = lastFMArtistData.artisInfo,
            infoURL = lastFMArtistData.artistUrl,
            source = LASTFMSOURCE,
            sourceLogoURL = LASTFMLOGO,
        )
    }
}

