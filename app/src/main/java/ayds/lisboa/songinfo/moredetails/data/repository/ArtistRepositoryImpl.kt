package ayds.lisboa.songinfo.moredetails.data.repository

import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.EmptyCard

class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val broker: ArtistBroker
) : ArtistRepository {

    override fun getArtistData(artistName: String): Card {
        var artistData = artistLocalStorage.getArtist(artistName)

        when {
            artistData != null -> artistData.markArtistAsLocal()
            else -> {
                try {
                    val cardList = broker.getCard(artistName)
                    for(card in cardList)
                        if(card is CardData)
                            artistLocalStorage.saveArtist(card)
                } catch (e: Exception) {
                    artistData = null
                }
            }
        }
        return artistData ?: EmptyCard
    }

    private fun CardData.markArtistAsLocal() {
        isLocallyStored = true
    }
}


