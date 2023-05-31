package ayds.lisboa.songinfo.moredetails.data.repository.proxys

import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.lisboa.songinfo.moredetails.data.repository.ServiceProxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

private const val NYTIMESLOGO =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

class NYTimesProxy(
    private val nyTimesService: NYTimesService
) : ServiceProxy {

    override fun getCardFromService(artist: String): Card? {
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
                sourceLogoURL = NYTIMESLOGO,
            )
        }
    }

}
