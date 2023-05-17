import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoHelper
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoHelperImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistInfoHelperImplTest {

    private val artistInfoHelper: ArtistInfoHelper = ArtistInfoHelperImpl()

    @Test
    fun `given text with special characters and term, it should return formatted HTML`() {
        val text = "La canción High Hopes fue escrita por el grupo de rock británico Pink Floyd. Fue lanzada en 1994 como parte de su álbum The Division Bell."
        val term = "Pink Floyd"

        val result = artistInfoHelper.textToHtml(text, term)

        val expected =
            "<html><div width=400><font face=\"arial\">La canción High Hopes fue escrita por el grupo de rock británico <b>PINK FLOYD</b>. Fue lanzada en 1994 como parte de su álbum \"The Division Bell\".</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `textToHtml should handle special characters and line breaks correctly`() {
        val text = "Este es un ejemplo que contiene\nsaltos de líneas que deben\nrespetarse en el formato final\n devuelto"
        val term = "líneas"

        val expectedResult =
            "<html><div width=400><font face=\"arial\">Este es un ejemplo que contiene<br>saltos de <b>LÍNEAS</b> que deben<br>respetarse en el formato final<br> devuelto</font></div></html>"

        val result = artistInfoHelper.textToHtml(text, term)

        assertEquals(expectedResult, result)
    }
}

