package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Subject

private const val LOCALLY_SAVED = "[*]"
private const val NO_RESULTS = "No results"

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MoreDetailsUiState>
    fun getArtistMoreInformation(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistRepository, private val artistInfoHelper: ArtistInfoHelper
) : MoreDetailsPresenter {

    override val uiStateObservable = Subject<MoreDetailsUiState>()
    private var uiState: MoreDetailsUiState = MoreDetailsUiState(emptyList())

    override fun getArtistMoreInformation(artistName: String) {
        Thread {
            notifyUIState(artistName)
        }.start()
    }

    private fun notifyUIState(artistName: String) {
        val artistCardsData = repository.getArtistData(artistName)
        artistCardsData.formattingDescriptionCardsArtist(artistName)
        artistCardsData.addLocallySavedMarkCardsArtist()
        updateUIState(artistCardsData)
        uiStateObservable.notify(uiState)
    }

    private fun List<CardData>.formattingDescriptionCardsArtist(artistName: String) {
        this.forEach {
            it.description = artistInfoHelper.textToHtml(it.description, artistName)
        }
    }

    private fun List<CardData>.addLocallySavedMarkCardsArtist() {
        this.forEach {
            if (it.isLocallyStored) it.description = LOCALLY_SAVED + it.description
        }
    }

    private fun updateUIState(artistCardList: List<CardData>) {
        when {
            artistCardList.isEmpty() -> updateNoResultsUiState()
            else -> updateCardsUIState(artistCardList)
        }
    }

    private fun updateNoResultsUiState() {
        val cardDataArtistNoResults: MutableList<CardDataState> = mutableListOf()
        cardDataArtistNoResults.add(
            CardDataState(
                artistName = "",
                description = NO_RESULTS,
                infoURL = "",
                source = "",
                sourceLogo = ""
            )
        )

        uiState.artistCards = cardDataArtistNoResults
    }

    private fun updateCardsUIState(artistCards: List<CardData>) {
        val cardsState: MutableList<CardDataState> = mutableListOf()

        artistCards.forEach {
            cardsState.add(
                CardDataState(
                    artistName = it.artistName,
                    description = it.description,
                    infoURL = it.infoURL,
                    source = it.source.name,
                    sourceLogo = it.sourceLogoURL
                )
            )
        }

        uiState.artistCards = cardsState
    }

}


