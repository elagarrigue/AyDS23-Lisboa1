package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterTest {
    private val repository : CardRepository = mockk(relaxUnitFun = true)
    private val helper: ArtistInfoHelper = mockk(relaxUnitFun = true)

    private val moreDetailsPresenter: MoreDetailsPresenter by lazy {
        MoreDetailsPresenterImpl(repository, helper)
    }

    @Test
    fun `given an empty artist, it should notify UIState without information`() {
        val artistName = "artist"
        every { repository.getArtistData(artistName) } returns Artist.EmptyArtist
        val artistTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.artistObservable.subscribe {
            artistTester(it)
        }

        moreDetailsPresenter.getArtistMoreInformation(artistName)

        verify {artistTester(MoreDetailsUiState("","No results",""))}
    }

    @Test
    fun `given a not locally stored artist, it should notify UIState with artist information`() {
        val artistData = Artist.ArtistData("artist", "info", "url", false)
        every { repository.getArtistData("artist") } returns artistData
        every { helper.textToHtml("info","artist") } returns "info"
        val artistTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.artistObservable.subscribe {
            artistTester(it)
        }

        moreDetailsPresenter.getArtistMoreInformation("artist")

        verify {artistTester(MoreDetailsUiState("artist","info","url"))}
    }

    @Test
    fun `given a locally stored artist, it should notify UIState with locally stored mark and artist information`() {
        val artistData = Artist.ArtistData("artist", "info", "url", true)
        every { repository.getArtistData("artist") } returns artistData
        every { helper.textToHtml("info","artist") } returns "info"
        val artistTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.artistObservable.subscribe {
            artistTester(it)
        }

        moreDetailsPresenter.getArtistMoreInformation("artist")

        verify {artistTester(MoreDetailsUiState("artist","[*] info","url"))}
    }

}