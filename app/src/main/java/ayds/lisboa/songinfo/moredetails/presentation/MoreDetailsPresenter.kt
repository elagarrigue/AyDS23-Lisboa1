package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.CardData
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

private const val LOCALLY_SAVED = "[*]"
private const val NO_RESULTS = "No results"

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MoreDetailsUiState>
    fun getCardMoreInformation(cardName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: CardRepository, private val cardInfoHelper: CardInfoHelper
) : MoreDetailsPresenter {

    override val uiStateObservable = Subject<MoreDetailsUiState>()
    private var uiState: MoreDetailsUiState = MoreDetailsUiState(emptyList())

    override fun getCardMoreInformation(cardName: String) {
        Thread {
            notifyUIState(cardName)
        }.start()
    }

    private fun notifyUIState(cardName: String) {
        val cardsData = repository.getCardData(cardName)
        cardsData.formattingDescriptionCards(cardName)
        cardsData.addLocallySavedMarkCards()
        updateUIState(cardsData)
        uiStateObservable.notify(uiState)
    }

    private fun List<CardData>.formattingDescriptionCards(cardName: String) {
        this.forEach {
            it.description = cardInfoHelper.textToHtml(it.description, cardName)
        }
    }

    private fun List<CardData>.addLocallySavedMarkCards() {
        this.forEach {
            if (it.isLocallyStored) it.description = LOCALLY_SAVED + it.description
        }
    }

    private fun updateUIState(cardList: List<CardData>) {
        when {
            cardList.isEmpty() -> updateNoResultsUiState()
            else -> updateCardsUIState(cardList)
        }
    }

    private fun updateNoResultsUiState() {
        val cardDataNoResults: MutableList<CardDataState> = mutableListOf()
        cardDataNoResults.add(
            CardDataState(
                cardName = "",
                description = NO_RESULTS,
                infoURL = "",
                sourceName = "",
                sourceLogo = ""
            )
        )

        uiState.cardsDataState = cardDataNoResults
    }

    private fun updateCardsUIState(cardsData: List<CardData>) {
        val cardsState: MutableList<CardDataState> = mutableListOf()

        cardsData.forEach {
            cardsState.add(
                CardDataState(
                    cardName = it.cardName,
                    description = it.description,
                    infoURL = it.infoURL,
                    sourceName = it.source.name,
                    sourceLogo = it.sourceLogoURL
                )
            )
        }

        uiState.cardsDataState = cardsState
    }

}


