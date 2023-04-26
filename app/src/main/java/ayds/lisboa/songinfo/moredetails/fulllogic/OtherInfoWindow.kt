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
    private var artistInfoPanel: TextView? = null
    private var dataBase: DataBase? = null

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        open(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

    private fun open(artist: String?) {
        initProperties()
        getArtistInfo(artist)
    }

    private fun initProperties() {
        setContentView(R.layout.activity_other_info)
        dataBase = DataBase(this)
        artistInfoPanel = findViewById(R.id.textPane2)
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            showInfoArtistFromSource(artistName)
        }.start()
    }

    private fun showInfoArtistFromSource(artistName: String?) {
        var infoArtist = getInfoArtistFromDatabase(artistName)
        if (infoArtist == null) {
            val jObjectArtist = artistName.getJObjectArtist()
            infoArtist = jObjectArtist.getInfoArtistFromJsonAPI(artistName)
            jObjectArtist.initializeButtonToOtherWindow()
        }
        showArtistInfo(infoArtist)
    }

    private fun getInfoArtistFromDatabase(artistName: String?): String? {
        val infoArtist = DataBase.getInfo(dataBase, artistName)
        return if (infoArtist != null) "[*]$infoArtist" else null
    }

    private fun String?.getJObjectArtist(): JsonObject {
        val callResponse: Response<String> = createLastFMAPI().getArtistInfo(this).execute()
        return Gson().fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun createLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder().baseUrl(ARTIST_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun JsonObject.getInfoArtistFromJsonAPI(artistName: String?): String {
        val infoArtist = this.getFormattingDataArtist(artistName) ?: NO_RESULTS
        try {
            if (infoArtist != NO_RESULTS) {
                DataBase.saveArtist(dataBase, artistName, infoArtist)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return infoArtist
    }

    private fun JsonObject.getFormattingDataArtist(artistName: String?): String? {
        var formattedInfoArtist: String? = null
        val contentArtist = this.getArtistBioContent()
        if (contentArtist != null) {
            val dataArtistString = contentArtist.asString.replace("\\n", "\n")
            formattedInfoArtist = textToHtml(dataArtistString, artistName)
        }
        return formattedInfoArtist
    }

    private fun JsonObject.getArtistBioContent(): JsonElement? {
        val artistObj = this[ARTIST_CONST].asJsonObject
        val bioObj = artistObj[BIO_ARTIST_CONST].asJsonObject
        return bioObj[CONTENT_ARTIST_CONST]
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text.replace("'", " ").replace("\n", "<br>").replace(
            "(?i)$term".toRegex(), "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
        )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    private fun JsonObject.initializeButtonToOtherWindow() {
        val artistObj = this[ARTIST_CONST].asJsonObject
        val urlArtist = artistObj[URL_ARTIST_CONST].asString
        urlArtist?.setOpenUrlButtonClickListener()
    }

    private fun String.setOpenUrlButtonClickListener() {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(this)
            startActivity(intent)
        }
    }

    private fun showArtistInfo(infoArtist: String?) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            artistInfoPanel?.text = infoArtist?.let { HtmlCompat.fromHtml(it, 0) }
        }
    }

}