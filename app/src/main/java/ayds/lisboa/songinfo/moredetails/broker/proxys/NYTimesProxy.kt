package ayds.lisboa.songinfo.moredetails.broker.proxys

import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.data.entities.NYT_LOGO_URL
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

class NYTimesProxy(
    private val nyTimesService: NYTimesService
) : ServiceProxy {

    override fun getCardFromService(artist: String): Card {
        val artistData = nyTimesService.getArtistInfo(artist)
        return mapNYTimesArtistToCard(artistData)
    }

    private fun mapNYTimesArtistToCard(nyTimesArtistData: ArtistDataExternal): Card {
        return (nyTimesArtistData as ArtistDataExternal.ArtistWithDataExternal).let {
            Card.CardData(
                artistName = nyTimesArtistData.name.toString(),
                description = nyTimesArtistData.info.toString(),
                infoURL = nyTimesArtistData.url,
                source = Card.Source.NYTimes,
                sourceLogoURL = NYT_LOGO_URL,
            )
        }
    }

}
