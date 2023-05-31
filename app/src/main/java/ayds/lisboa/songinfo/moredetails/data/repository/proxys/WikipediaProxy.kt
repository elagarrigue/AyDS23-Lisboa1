package ayds.lisboa.songinfo.moredetails.data.repository.proxys

import ayds.lisboa.songinfo.moredetails.data.repository.ServiceProxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

private const val WIKIPEDIASOURCE = "Wikipedia"

class WikipediaProxy(
    private val wikipediaService: WikipediaTrackService
): ServiceProxy {

    override fun getCardFromService(artist: String): Card? {
        val artistData = wikipediaService.getInfo(artist)
        return artistData?.let {
            mapWikipediaArtistToCard(artist, artistData)
        }
    }

    private fun mapWikipediaArtistToCard(artist: String, wikipediaArtistData: ArtistInfo): Card {
        return Card.CardData(
            artistName = artist,
            description = wikipediaArtistData.description,
            infoURL = wikipediaArtistData.wikipediaURL,
            source = WIKIPEDIASOURCE,
            sourceLogoURL = wikipediaArtistData.wikipediaLogoURL,
        )
    }
}