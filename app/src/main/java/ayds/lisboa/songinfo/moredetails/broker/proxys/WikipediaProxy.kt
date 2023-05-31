package ayds.lisboa.songinfo.moredetails.broker.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

class WikipediaProxy(
    private val wikipediaService: WikipediaTrackService
) : ServiceProxy {

    override fun getCardFromService(artist: String): Card {
        val artistData = wikipediaService.getInfo(artist)
            ?: throw Exception("Error al obtener los datos del artista")
        return mapWikipediaArtistToCard(artist, artistData)
    }

    private fun mapWikipediaArtistToCard(artist: String, wikipediaArtistData: ArtistInfo): Card {
        return Card.CardData(
            artistName = artist,
            description = wikipediaArtistData.description,
            infoURL = wikipediaArtistData.wikipediaURL,
            source = Card.Source.Wikipedia,
            sourceLogoURL = wikipediaArtistData.wikipediaLogoURL,
        )
    }
}