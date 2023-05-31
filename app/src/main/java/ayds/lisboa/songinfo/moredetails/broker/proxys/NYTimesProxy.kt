package ayds.lisboa.songinfo.moredetails.broker.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.EmptyCard
import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.data.entities.NYT_LOGO_URL

class NYTimesProxy(
    private val nyTimesService: NYTimesService
) : ServiceProxy {

    override fun getCardFromService(artist: String): Card {
        val artistCard =
            try {
                val artistData = nyTimesService.getArtistInfo(artist)
                mapNYTimesArtistToCard(artistData)
            }
            catch(e: Exception) {
                EmptyCard
            }
        return artistCard
    }

    private fun mapNYTimesArtistToCard(nyTimesArtistData: ArtistDataExternal): Card {
        return (nyTimesArtistData as ArtistDataExternal.ArtistWithDataExternal).let {
            CardData(
                artistName = nyTimesArtistData.name.toString(),
                description = nyTimesArtistData.info.toString(),
                infoURL = nyTimesArtistData.url,
                source = Card.Source.NYTimes,
                sourceLogoURL = NYT_LOGO_URL,
            )
        }
    }

}
