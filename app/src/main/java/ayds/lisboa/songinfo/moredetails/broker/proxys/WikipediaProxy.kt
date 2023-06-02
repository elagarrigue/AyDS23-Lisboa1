package ayds.lisboa.songinfo.moredetails.broker.proxys

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
                val artistData = (wikipediaService.getInfo(cardName) as ArtistInfo)
                mapWikipediaArtistToCard(cardName, artistData)
            }
            catch(e: Exception) {
                EmptyCard
            }
        return card
    }

    private fun mapWikipediaArtistToCard(artist: String, wikipediaArtistData: ArtistInfo): Card {
        return CardData(
            cardName = artist,
            description = wikipediaArtistData.description,
            infoURL = wikipediaArtistData.wikipediaURL,
            source = Card.Source.Wikipedia,
            sourceLogoURL = wikipediaArtistData.wikipediaLogoURL,
        )
    }
}