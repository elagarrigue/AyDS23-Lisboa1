package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
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
    private var uiState: MoreDetailsUiState = MoreDetailsUiState()

    override fun getArtistMoreInformation(artistName: String) {
        Thread {
            notifyArtist(artistName)
        }.start()
    }

    private fun notifyArtist(artistName: String){
        val artistData: Card = repository.getArtistData(artistName)
        artistData.applyFormattingInfoArtist(artistName)
        artistData.addLocallySavedMarkToInfo()
        updateUIState(artistData)
        artistObservable.notify(uiState)
    }

    private fun updateUIState(artist: Card) {
        when (artist) {
            is CardData -> updateArtistUIState(artist)
            Card.EmptyCard -> updateNoResultsUiState()
        }
    }
    private fun updateArtistUIState(artist: CardData) {

        uiState = uiState.copy(
            artistName = artist.artistName,
            infoArtist = artist.description,
            url = artist.infoURL
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            artistName = "",
            infoArtist = NO_RESULTS,
            url = ""
        )
    }

    private fun Card.applyFormattingInfoArtist(artistName: String) {
        if( this is CardData){
            this.description = artistInfoHelper.textToHtml(this.description, artistName)
        }
    }

    private fun Card.addLocallySavedMarkToInfo() {
     if (this is CardData) {
         if(isLocallyStored)
             description = "$LOCALLY_SAVED $description"
     }
 }
}


