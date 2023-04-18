package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl (private val formatterFactory:FormatterFactory) : SongDescriptionHelper {

    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release Date: ${getReleaseDateString(song.releaseDatePrecision, song.releaseDate)}"
            else -> "Song not found"
        }
    }

    private fun getReleaseDateString(releaseDatePrecision : String,releaseDate : String) : String{
        val formatter = formatterFactory.getWrapper(releaseDatePrecision)
        return formatter.getReleaseDateFormat(releaseDate)

    }
}