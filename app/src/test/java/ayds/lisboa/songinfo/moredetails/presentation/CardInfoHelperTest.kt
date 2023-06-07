package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class CardInfoHelperImplTest {

    private val sourceFactory :  SourceFactory = mockk(relaxUnitFun = true)
    private val cardDescriptionHelper: CardDescriptionHelper = CardDescriptionHelperImpl(sourceFactory)

    @Test
    fun `given text with the term, it should should handle special characters and line breaks correctly to return formatted HTML`() {
        val text = "Este es un ejemplo que contiene\nsaltos de líneas que deben\nrespetarse en el formato final\n devuelto"
        val term = "líneas"

        val expectedResult =
            "<html><div width=400><font face=\"arial\">Este es un ejemplo que contiene<br>saltos de <b>LÍNEAS</b> que deben<br>respetarse en el formato final<br> devuelto</font></div></html>"

        val result = cardDescriptionHelper.textToHtml(text, term)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `given source, it should return source formatted to String`() {
        val source: Card.Source = mockk ()
        every {sourceFactory.getSourceString(source)} returns "source"

        val expectedResult = "source"
        val result = cardDescriptionHelper.getSourceString(source)

        assertEquals(expectedResult, result)
    }

}