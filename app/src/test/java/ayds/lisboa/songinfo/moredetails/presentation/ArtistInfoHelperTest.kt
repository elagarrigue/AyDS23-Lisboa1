package ayds.lisboa.songinfo.moredetails.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistInfoHelperImplTest {

    private val artistInfoHelper: ArtistInfoHelper = ArtistInfoHelperImpl()

    @Test
    fun `given text with the term, it should should handle special characters and line breaks correctly to return formatted HTML`() {
        val text = "Este es un ejemplo que contiene\nsaltos de líneas que deben\nrespetarse en el formato final\n devuelto"
        val term = "líneas"

        val expectedResult =
            "<html><div width=400><font face=\"arial\">Este es un ejemplo que contiene<br>saltos de <b>LÍNEAS</b> que deben<br>respetarse en el formato final<br> devuelto</font></div></html>"

        val result = artistInfoHelper.textToHtml(text, term)

        assertEquals(expectedResult, result)
    }
}