package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.repository.CardRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.repository.external.CardBroker
import ayds.lisboa.songinfo.moredetails.data.repository.local.CardLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class CardRepositoryTest {

    private val cardLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)
    private val cardBroker: CardBroker = mockk(relaxUnitFun = true)

    private val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(cardLocalStorage, cardBroker)
    }

    @Test
    fun `given existing card should return list of cards and mark them as local`() {
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
        every { cardLocalStorage.getCards("card") } returns cardsData

        val result = cardRepository.getCardData("card")

        assertSame(cardsData, result)
        assertTrue(result.all { it.isLocallyStored })
    }

    @Test
    fun `given non-existent card should get the list of cards and save each card`() {
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
        every { cardLocalStorage.getCards("card") } returns emptyList()
        every { cardBroker.getCard("card") } returns cardsData

        val result = cardRepository.getCardData("card")

        assertSame(cardsData, result)
        assertTrue(result.all { !it.isLocallyStored })
        verify { cardLocalStorage.saveCard(cardData1) }
        verify { cardLocalStorage.saveCard(cardData2) }
        verify { cardLocalStorage.saveCard(cardData3) }
    }

    @Test
    fun `given the non-existent card everywhere, it should return an empty list`() {
        every { cardLocalStorage.getCards("card") } returns emptyList()
        every { cardBroker.getCard("card") } returns emptyList()

        val result = cardRepository.getCardData("card")

        assertTrue(result.isEmpty())
    }

}