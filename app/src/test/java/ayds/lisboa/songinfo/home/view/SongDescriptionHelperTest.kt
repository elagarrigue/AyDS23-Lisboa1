package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class SongDescriptionHelperTest {

    private val formatterFactory : FormatterFactoryImpl = mockk(relaxUnitFun = true)
    private val songDescriptionHelper by lazy { SongDescriptionHelperImpl(formatterFactory) }

    @Test
    fun `given a local song it should return the description`() {
        every {formatterFactory.getWrapper("year")} returns DateFormatWrapperYear()
        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "url",
            "url",
            "year",
            true,
        )

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush [*]\n" +
                "Artist: Stone Temple Pilots\n" +
                "Album: Core\n" +
                "Release Date: 1992 (Leap year)"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local song it should return the description`() {
        every {formatterFactory.getWrapper("year")} returns DateFormatWrapperYear()
        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "url",
            "url",
            "year",
            false,
        )

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush \n" +
                "Artist: Stone Temple Pilots\n" +
                "Album: Core\n" +
                "Release Date: 1992 (Leap year)"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non spotify song it should return the song not found description`() {
        val song: Song = mockk()

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected = "Song not found"

        assertEquals(expected, result)
    }
}