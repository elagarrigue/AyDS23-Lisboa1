package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Subject

private const val LOCALLY_SAVED = "[*]"
private const val NO_RESULTS = "No results"

interface MoreDetailsPresenter {
    val artistObservable: Observable<MoreDetailsUiState>
    fun getArtistMoreInformation(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistRepository,
    private val artistInfoHelper : ArtistInfoHelper) :
    MoreDetailsPresenter {

    override val artistObservable = Subject<MoreDetailsUiState>()
    private var uiState : MoreDetailsUiState = MoreDetailsUiState(emptyList())

    override fun getArtistMoreInformation(artistName: String) {
        Thread {
            notifyArtist(artistName)
        }.start()
    }

    private fun notifyArtist(artistName: String){
        val artistCardsData = repository.getArtistData(artistName)
        artistCardsData.formattingDescriptionCardsArtist(artistName)
        artistCardsData.addLocallySavedMarkCardsArtist()
        updateUIState(artistCardsData)
        artistObservable.notify(uiState)
    }

    private fun updateUIState(artistCardList: List<CardData>) {
        when{
            !artistCardList.isEmpty()  -> updateArtistUIState(artistCardList)
        }
    }
    private fun updateArtistUIState(artistCards: List<CardData>) {
        var cardsState: MutableList<CardDataState> = mutableListOf()

        artistCards.forEach{
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

    private fun List<CardData>.formattingDescriptionCardsArtist(artistName: String) {
        this.forEach{
            it.description = artistInfoHelper.textToHtml(it.description, artistName)
        }
    }

    private fun List<CardData>.addLocallySavedMarkCardsArtist() {
        this.forEach{
            if(it.isLocallyStored)
                it.description= LOCALLY_SAVED + it.description
        }
    }
}


