package ayds.lisboa.songinfo.moredetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.data.repository.external.LastFMAPI
import ayds.lisboa.songinfo.utils.view.ImageLoader
import ayds.lisboa.songinfo.utils.view.ImageLoaderImpl
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class OtherInfoWindow : AppCompatActivity() {

    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var openURLListener: View
    private lateinit var artistInfoPanel: TextView
    private lateinit var dataBase: DataBase
    private lateinit var imageLastFMAPI: ImageView
    private lateinit var imageLoader: ImageLoader

    companion object {
        const val imageUrl =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        const val ARTIST_NAME_EXTRA = "artistName"
        const val ARTIST_CONST = "artist"
        const val BIO_ARTIST_CONST = "bio"
        const val CONTENT_ARTIST_CONST = "content"
        const val URL_ARTIST_CONST = "url"
        const val NO_RESULTS = "No results"
        const val ARTIST_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
        const val HTML_START = "<html><div width=400>"
        const val HTML_END = "</font></div></html>"
        const val FONT_FACE = "<font face=\"arial\">"
        const val LOCALLY_SAVED = "[*]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_other_info)
        initProperties()
        initImageLoader()
        initDataBase()
        initLastFMAPI()
        getMoreDetailsOfAnArtistAsync()
    }

    private fun initProperties() {
        artistInfoPanel = findViewById(R.id.textPane2)
        openURLListener = findViewById(R.id.openUrlButton)
        imageLastFMAPI = findViewById(R.id.imageView)
    }

    private fun initImageLoader() {
        imageLoader = ImageLoaderImpl(Picasso.get())
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
    }

    private fun initLastFMAPI() {
        lastFMAPI = createLastFMAPI()
    }

    private fun createLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder().baseUrl(ARTIST_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun getMoreDetailsOfAnArtistAsync() {
        Thread {
            getMoreDetailsOfAnArtist()
        }.start()
    }

    private fun getMoreDetailsOfAnArtist() {
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        val artistData = getArtistData(artistName)

        artistData.addLocallySavedMarkToInfo()
        initializeIUrlButton(artistData)
        showArtistInfo(artistData.infoArtist)
    }



    private fun initializeIUrlButton(artistData: ArtistData) {
        if (!artistData.isLocallyStored) {
            setOpenUrlButtonClickListener(artistData.url)
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

    private fun showArtistInfo(infoArtist: String?) {
        runOnUiThread {
            imageLoader.loadImageIntoView(imageUrl, imageLastFMAPI)
            artistInfoPanel.text = infoArtist?.let { HtmlCompat.fromHtml(it, 0) }
        }
    }