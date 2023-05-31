package ayds.lisboa.songinfo.moredetails.data.repository

import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal.ArtistWithDataExternal
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa1.lastfm.LastFMArtistData
import ayds.lisboa1.lastfm.LastFMService
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

private const val LASTFMLOGO =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val NYTIMESLOGO =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

interface ServiceProxy {
    fun getCardFromService(artist: String): Card?
}

internal class LastFMProxy(
    private val lastFMService: LastFMService
) : ServiceProxy {

    override fun getCardFromService(artist: String): Card? {
        val artistData = lastFMService.getArtistData(artist)
        return artistData?.let { mapLastFMArtistToCard(it) }
    }

    private fun mapLastFMArtistToCard(lastFMArtistData: LastFMArtistData): Card {
        return CardData(
            artistName = lastFMArtistData.artistName,
            description = lastFMArtistData.artisInfo,
            infoURL = lastFMArtistData.artistUrl,
            source = Card.Source.LASTFMSOURCE,
            sourceLogoURL = LASTFMLOGO,
        )
    }
}

internal class NYTimesProxy(
    private val nyTimesService: NYTimesService
) : ServiceProxy {

    override fun getCardFromService(artist: String): Card? {
        val artistData = nyTimesService.getArtistInfo(artist)
        return mapNYTimesArtistToCard(artistData)
    }

    private fun mapNYTimesArtistToCard(nyTimesArtistData: ArtistDataExternal): Card {
        return (nyTimesArtistData as ArtistWithDataExternal).let {
            CardData(
                artistName = nyTimesArtistData.name.toString(),
                description = nyTimesArtistData.info.toString(),
                infoURL = nyTimesArtistData.url,
                source = Card.Source.NYTIMESSOURCE,
                sourceLogoURL = NYTIMESLOGO,
            )
        }
    }
}

internal class WikipediaProxy(
    private val wikipediaService: WikipediaTrackService
) : ServiceProxy {

    override fun getCardFromService(artist: String): Card? {
        val artistData = wikipediaService.getInfo(artist)
        return artistData?.let {
            mapWikipediaArtistToCard(artist, artistData)
        }
    }

    private fun mapWikipediaArtistToCard(artist: String, wikipediaArtistData: ArtistInfo): Card {
        return CardData(
            artistName = artist,
            description = wikipediaArtistData.description,
            infoURL = wikipediaArtistData.wikipediaURL,
            source = Card.Source.WIKIPEDIASOURCE,
            sourceLogoURL = wikipediaArtistData.wikipediaLogoURL,
        )
    }
    
}