package moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoHelper
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.lisboa.songinfo.moredetails.presentation.MoreDetailsUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterTest {
    private val repository : ArtistRepository = mockk()
    private val helper: ArtistInfoHelper = mockk(relaxUnitFun = true)

    private val moreDetailsPresenter: MoreDetailsPresenter by lazy {
        MoreDetailsPresenterImpl(repository, helper)
    }

    @Test
    fun `on get artist information, it should notify the result`() {
        val artistName = "artist"
        val artistData: Artist = mockk()
        every { repository.getArtistData(artistName) } returns artistData

        val artistTester: (MoreDetailsUiState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.artistObservable.subscribe() {
            artistTester(it)
        }

        moreDetailsPresenter.getArtistMoreInformation(artistName)

        verify { artistTester(any()) }
    }
}