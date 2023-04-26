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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        open(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            showInfoArtistFromSource(artistName)
        }.start()
    }

    private fun open(artist: String?) {
        initProperties()
        getArtistInfo(artist)
    }

    private fun showInfoArtistFromSource(artistName: String?){
        val infoArtist = getInfoArtistFromDatabase(artistName) ?: artistName.getInfoArtistFromAPI()
        showArtistInfo(infoArtist)
    }

    private fun getInfoArtistFromDatabase(artistName: String?): String? {
        val infoArtist = DataBase.getInfo(dataBase, artistName)
        return if (infoArtist != null) "[*]$infoArtist" else null
    }

    private fun String?.getInfoArtistFromAPI(): String?{
        var infoArtist = NO_RESULTS
        val jObjectArtist = this.getJObjectArtist()
        try {
            if (jObjectArtist.getArtistBioContent() != null) {
                infoArtist = textToHtml(
                    jObjectArtist.getArtistBioContent().
                    asString.replace("\\n", "\n"), this
                )
                DataBase.saveArtist(dataBase, this, infoArtist)
            }
            jObjectArtist.getArtistURL().asString.setOpenUrlButtonClickListener()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return infoArtist
    }

    private fun JsonObject.getArtistBioContent() = this[ARTIST_CONST].asJsonObject[BIO_ARTIST_CONST].asJsonObject[CONTENT_ARTIST_CONST]
    private fun JsonObject.getArtistURL() = this[ARTIST_CONST].asJsonObject[URL_ARTIST_CONST]

    private fun createLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder().baseUrl(ARTIST_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun String?.getJObjectArtist(): JsonObject {
        val callResponse: Response<String> = createLastFMAPI().getArtistInfo(this).execute()
        return Gson().fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun showArtistInfo(infoArtist: String?) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            infoArtist?.let { HtmlCompat.fromHtml(it, 0) }
        }
    }

    private fun String.setOpenUrlButtonClickListener() {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(this)
            startActivity(intent)
        }
    }

    private fun initProperties() {
        setContentView(R.layout.activity_other_info)
        dataBase = DataBase(this)
        artistInfoPanel = findViewById(R.id.textPane2)
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

    companion object {
        const val imageUrl =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        const val ARTIST_NAME_EXTRA = "artistName"
        const val ARTIST_CONST = "artist"
        const val BIO_ARTIST_CONST = "bio"
        const val CONTENT_ARTIST_CONST = "content"
        const val URL_ARTIST_CONST = "URL"
        const val NO_RESULTS = "No results"
        const val ARTIST_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
    }
}