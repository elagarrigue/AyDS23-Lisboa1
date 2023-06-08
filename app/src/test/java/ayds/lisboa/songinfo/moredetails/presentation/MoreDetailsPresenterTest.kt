package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterTest {
    private val repository: CardRepository = mockk(relaxUnitFun = true)
    private val helper: CardDescriptionHelper = mockk(relaxUnitFun = true)

    private val moreDetailsPresenter: MoreDetailsPresenter by lazy {
        MoreDetailsPresenterImpl(repository, helper)
    }

    @Test
    fun `given a emptyList of cards, it should notify UIState without information`() {
        val cardName = "card"
        every { repository.getCardData(cardName) } returns emptyList()
        val cardTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe {
            cardTester(it)
        }

        moreDetailsPresenter.getCardMoreInformation(cardName)

        val cardDataState = CardDataState("", "No results", "", "", "")
        val cardsDataState = listOf(cardDataState)
        verify { cardTester(MoreDetailsUiState(cardsDataState, 0)) }
    }

    @Test
    fun `given a list of not locally stored cards, it should notify UIState with card information`() {
        val cardData1 = Card.CardData(
            "card", "description1", "url1", Card.Source.LastFM, "sourceLogoURL1", false
        )
        val cardData2 = Card.CardData(
            "card", "description2", "url2", Card.Source.NYTimes, "sourceLogoURL2", false
        )
        val cardData3 = Card.CardData(
            "card", "description3", "url3", Card.Source.Wikipedia, "sourceLogoURL3", false
        )
        val cardsData = listOf(cardData1, cardData2, cardData3)
        every { repository.getCardData("card") } returns cardsData
        every { helper.textToHtml("description1", "card") } returns "description1"
        every { helper.textToHtml("description2", "card") } returns "description2"
        every { helper.textToHtml("description3", "card") } returns "description3"
        every { helper.getSourceString(Card.Source.LastFM) } returns "LastFM"
        every { helper.getSourceString(Card.Source.NYTimes) } returns "New York Times"
        every { helper.getSourceString(Card.Source.Wikipedia) } returns "Wikipedia"

        val cardTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe {
            cardTester(it)
        }

        moreDetailsPresenter.getCardMoreInformation("card")

        val cardDataState1 =
            CardDataState("card", "description1", "url1", "LastFM", "sourceLogoURL1")
        val cardDataState2 =
            CardDataState("card", "description2", "url2", "New York Times", "sourceLogoURL2")
        val cardDataState3 =
            CardDataState("card", "description3", "url3", "Wikipedia", "sourceLogoURL3")
        val cardsDataState = listOf(cardDataState1, cardDataState2, cardDataState3)
        verify { cardTester(MoreDetailsUiState(cardsDataState, 0)) }
    }

    @Test
    fun `given a list of locally stored cards, it should notify UIState with locally stored mark and card information`() {
        val cardData1 = Card.CardData(
            "card", "description1", "url1", Card.Source.LastFM, "sourceLogoURL1", true
        )
        val cardData2 = Card.CardData(
            "card", "description2", "url2", Card.Source.NYTimes, "sourceLogoURL2", true
        )
        val cardData3 = Card.CardData(
            "card", "description3", "url3", Card.Source.Wikipedia, "sourceLogoURL3", true
        )
        val cardsData = listOf(cardData1, cardData2, cardData3)
        every { repository.getCardData("card") } returns cardsData
        every { helper.textToHtml("description1", "card") } returns "description1"
        every { helper.textToHtml("description2", "card") } returns "description2"
        every { helper.textToHtml("description3", "card") } returns "description3"
        every { helper.getSourceString(Card.Source.LastFM) } returns "LastFM"
        every { helper.getSourceString(Card.Source.NYTimes) } returns "New York Times"
        every { helper.getSourceString(Card.Source.Wikipedia) } returns "Wikipedia"

        val cardTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe {
            cardTester(it)
        }

        moreDetailsPresenter.getCardMoreInformation("card")

        val cardDataState1 =
            CardDataState("card", "[*]description1", "url1", "LastFM", "sourceLogoURL1")
        val cardDataState2 =
            CardDataState("card", "[*]description2", "url2", "New York Times", "sourceLogoURL2")
        val cardDataState3 =
            CardDataState("card", "[*]description3", "url3", "Wikipedia", "sourceLogoURL3")
        val cardsDataState = listOf(cardDataState1, cardDataState2, cardDataState3)
        verify { cardTester(MoreDetailsUiState(cardsDataState, 0)) }
    }

    @Test
    fun `given a list of cards, it should notify UIState with locally stored mark and card information if applicable`() {
        val cardData1 = Card.CardData(
            "card", "description1", "url1", Card.Source.LastFM, "sourceLogoURL1", false
        )
        val cardData2 = Card.CardData(
            "card", "description2", "url2", Card.Source.NYTimes, "sourceLogoURL2", true
        )
        val cardData3 = Card.CardData(
            "card", "description3", "url3", Card.Source.Wikipedia, "sourceLogoURL3", false
        )
        val cardsData = listOf(cardData1, cardData2, cardData3)
        every { repository.getCardData("card") } returns cardsData
        every { helper.textToHtml("description1", "card") } returns "description1"
        every { helper.textToHtml("description2", "card") } returns "description2"
        every { helper.textToHtml("description3", "card") } returns "description3"
        every { helper.getSourceString(Card.Source.LastFM) } returns "LastFM"
        every { helper.getSourceString(Card.Source.NYTimes) } returns "New York Times"
        every { helper.getSourceString(Card.Source.Wikipedia) } returns "Wikipedia"

        val cardTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe {
            cardTester(it)
        }

        moreDetailsPresenter.getCardMoreInformation("card")

        val cardDataState1 =
            CardDataState("card", "description1", "url1", "LastFM", "sourceLogoURL1")
        val cardDataState2 =
            CardDataState("card", "[*]description2", "url2", "New York Times", "sourceLogoURL2")
        val cardDataState3 =
            CardDataState("card", "description3", "url3", "Wikipedia", "sourceLogoURL3")
        val cardsDataState = listOf(cardDataState1, cardDataState2, cardDataState3)
        verify { cardTester(MoreDetailsUiState(cardsDataState, 0)) }
    }

}

