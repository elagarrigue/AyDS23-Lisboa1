package ayds.lisboa.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist.ArtistData
import ayds.lisboa.songinfo.moredetails.injector.MoreDetailsInjector
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.view.ImageLoader

private const val LOCALLY_SAVED = "[*]"
class MoreDetailsViewActivity : AppCompatActivity() {

    private lateinit var artistInfoPanel: TextView
    private lateinit var openURLListener: View
    private lateinit var imageLastFMAPI: ImageView
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader

    private var uiState: MoreDetailsUiState = MoreDetailsUiState()

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initModule()
        initObservers()
        setContentView(R.layout.activity_other_info)
        initProperties()
        initArtistName()
    }

    private fun showArtistInfo(infoArtist: String?) {
        runOnUiThread {
            imageLoader.loadImageIntoView(uiState.imageUrl, imageLastFMAPI)
            artistInfoPanel.text = infoArtist?.let { HtmlCompat.fromHtml(it, 0) }
        }
    }

    private fun initArtistName() {
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        artistName?.let { moreDetailsPresenter.getArtistMoreInformation(it) }
    }

    private fun initModule() {
        MoreDetailsInjector.initMoreDetailsPresenter(this)
        moreDetailsPresenter = MoreDetailsInjector.getPresenter()
    }

    private fun initProperties() {
        artistInfoPanel = findViewById(R.id.artistInfoPanel)
        openURLListener = findViewById(R.id.openUrlButton)
        imageLastFMAPI = findViewById(R.id.imageView)
    }

    private fun initObservers() {
        moreDetailsPresenter.artistObservable.subscribe { value -> updateArtistInfo(value) }
    }

    private fun updateArtistInfo(artist: Artist) {
        updateUIState(artist)
        uiState.addLocallySavedMarkToInfo()
        setOpenUrlButtonClickListener(uiState.url)
        showArtistInfo(uiState.infoArtist)
    }

    private fun updateUIState(artist: Artist) {
        when (artist) {
            is ArtistData -> updateArtistUIState(artist)
            Artist.EmptyArtist -> updateNoResultsUiState()
        }
    }

    private fun updateArtistUIState(artist: ArtistData) {

        uiState = uiState.copy(
            artistName = artist.artistName,
            infoArtist = artist.infoArtist,
            url = artist.url
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            artistName = "",
            infoArtist = "",
            url = ""
        )
    }

    private fun MoreDetailsUiState.addLocallySavedMarkToInfo() {
        if (isLocallyStored) {
            infoArtist = "$LOCALLY_SAVED $infoArtist"
        }
    }

    private fun setOpenUrlButtonClickListener(artistURL: String) {
        openURLListener.setOnClickListener {
            openURL(artistURL)
        }
    }

    private fun openURL(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}