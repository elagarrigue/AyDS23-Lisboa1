package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.utils.view.ImageLoader
import ayds.lisboa.songinfo.utils.view.ImageLoaderImpl
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

class OtherInfoWindow : AppCompatActivity() {

    private var lastFMAPI: LastFMAPI? = null
    private lateinit var openURLListener: View
    private lateinit var artistInfoPanel: TextView
    private lateinit var dataBase: DataBase
    private lateinit var imageLastFMAPI: ImageView
    private lateinit var imageLoader : ImageLoader

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
        val artisName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        setContentView(R.layout.activity_other_info)

        initProperties()
        initImageLoader()
        initDataBase()
        initLastFMAPI()
        open(artisName)
    }

    private fun initProperties() {
        artistInfoPanel = findViewById(R.id.textPane2)
        openURLListener = findViewById(R.id.openUrlButton)
        imageLastFMAPI = findViewById(R.id.imageView)
    }

    private fun initImageLoader(){
        imageLoader = ImageLoaderImpl(Picasso.get())
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
    }

    private fun initLastFMAPI() {
        lastFMAPI = createLastFMAPI()
    }

    private fun open(artist: String?) {
        getMoreDetailsOfAnArtistAsync(artist)
    }

    private fun getMoreDetailsOfAnArtistAsync(artistName: String?) {
        Thread {
            getMoreDetailsOfAnArtist(artistName)
        }.start()
    }

    private fun getMoreDetailsOfAnArtist(artistName: String?) {
        val artistData = getArtistData(artistName)
        checkToInitializeTheButton(artistData)
        showArtistInfo(artistData.infoArtist)
    }

    private fun checkToInitializeTheButton(artistData : ArtistData) {
        if (!artistData.isLocallyStored) {
            setOpenUrlButtonClickListener(artistData.url)
        }
    }

    private fun getArtistData(artistName: String?): ArtistData {
        val infoArtistData = getInfoArtistFromDatabase(artistName)
        if (infoArtistData.infoArtist != null) {
            infoArtistData.markArtistAsLocal()
        } else {
            infoArtistData.getArtistFromAPI()
            infoArtistData.saveInDataBase()
        }
        return infoArtistData
    }

    private fun ArtistData.saveInDataBase() {
        try {
            if (this.infoArtist != NO_RESULTS) {
                dataBase.saveArtist(artistName, infoArtist)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun ArtistData.getArtistFromAPI() {
        val jObjectArtist = this.getJObjectArtist()
        jObjectArtist.getInfoArtistFromJsonAPI(this)
    }

    private fun ArtistData.markArtistAsLocal() {
        isLocallyStored = true
    }

    private fun getInfoArtistFromDatabase(artistName: String?): ArtistData {
        val infoArtist = dataBase.getInfo(artistName)
        return ArtistData(artistName, infoArtist)
    }

    private fun ArtistData.getJObjectArtist(): JsonObject {
        val bodyResponse = getResponse()?.body()
        return artistResponseToJson(bodyResponse)
    }

    private fun ArtistData.getResponse(): Response<String>? {
        val artistInfo = lastFMAPI?.getArtistInfo(artistName)
        return artistInfo?.execute()
    }

    private fun artistResponseToJson(string: String?): JsonObject {
        return Gson().fromJson(string, JsonObject::class.java)
    }

    private fun createLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder().baseUrl(ARTIST_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun JsonObject.getInfoArtistFromJsonAPI(artistData: ArtistData) {
        artistData.infoArtist = this.getFormattingDataArtist(artistData.artistName) ?: NO_RESULTS
        artistData.url = this.getArtistURLFromJSON()
    }

    private fun JsonObject.getFormattingDataArtist(artistName: String?): String? {
        var formattedInfoArtist: String? = null
        val contentArtist = this.getArtistBioContent()
        if (contentArtist != null) {
            val dataArtistString = contentArtist.asString.replace("\\n", "\n")
            formattedInfoArtist = artistName?.let { textToHtml(dataArtistString, it) }
        }
        return formattedInfoArtist
    }

    private fun JsonObject.getArtistBioContent(): JsonElement? {
        val artistObj = this[ARTIST_CONST].asJsonObject
        val bioObj = artistObj[BIO_ARTIST_CONST].asJsonObject
        return bioObj[CONTENT_ARTIST_CONST]
    }

    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append(HTML_START)
        builder.append(FONT_FACE)
        val textWithBold = text.replace("'", " ").replace("\n", "<br>").replace(
            "(?i)$term".toRegex(), "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
        )
        builder.append(textWithBold)
        builder.append(HTML_END)
        return builder.toString()
    }

    private fun JsonObject.getArtistURLFromJSON(): String {
        val artistObj = this[ARTIST_CONST].asJsonObject
        return artistObj[URL_ARTIST_CONST].asString
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
            imageLoader.loadImageIntoView(imageUrl,imageLastFMAPI)
            artistInfoPanel.text = infoArtist?.let { HtmlCompat.fromHtml(it, 0) }
        }
    }

}

data class ArtistData(
    var artistName: String?,
    var infoArtist: String?,
    var url: String = "",
    var isLocallyStored: Boolean = false
)