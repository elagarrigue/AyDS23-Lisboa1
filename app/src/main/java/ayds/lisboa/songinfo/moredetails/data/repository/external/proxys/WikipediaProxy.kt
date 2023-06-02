package ayds.lisboa.songinfo.moredetails.data.repository.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.EmptyCard
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

class WikipediaProxy(
    private val wikipediaService: WikipediaTrackService
) : ServiceProxy {

    override fun getCardFromService(cardName: String): Card {
        val card =
            try {
                val artistData = wikipediaService.getInfo(cardName)
                mapWikipediaArtistToCard(cardName, artistData)
            }
            catch(e: Exception) {
                null
            }
        return card?: EmptyCard
    }

    private fun mapWikipediaArtistToCard(cardName: String, wikipediaArtistData: ArtistInfo?): Card? {
        return wikipediaArtistData?.let {
            CardData(
                cardName = cardName,
                description = wikipediaArtistData.description,
                infoURL = wikipediaArtistData.wikipediaURL,
                source = Card.Source.Wikipedia,
                sourceLogoURL = wikipediaArtistData.wikipediaLogoURL,
            )
        }
    }
}