package ayds.lisboa.songinfo.home.view


interface DateFormatWrapper {

    fun releaseDateFormat(releaseDate: String, precision: String): String

}

internal class DateFormatWrapperImpl : DateFormatWrapper {

     override fun releaseDateFormat(releaseDate: String, precision: String): String {
         val splitReleaseDate = releaseDate.split("-")
         return when (precision) {
             "year" -> splitReleaseDate.first() +
                     if (isLeapYear(Integer.parseInt(releaseDate))) {
                         " (Leap year)"
                     } else {
                         " (Not a leap year)"
                     }

             "month" -> getMonth(splitReleaseDate[1]) +
                     ", " + splitReleaseDate.first()

             else -> splitReleaseDate[2] + "/" + splitReleaseDate[1] + "/" + splitReleaseDate[0]
         }
     }

    private fun isLeapYear(year: Int): Boolean {
        return (((year % 4) == 0) && (((year % 100) != 0) || ((year % 400) == 0)))
    }

    private fun getMonth(monthNumber: String): String {
        return when(monthNumber) {
            "01" -> "January"
            "02" -> "February"
            "03" -> "March"
            "04" -> "April"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "August"
            "09" -> "September"
            "10" -> "October"
            "11" -> "November"
            else -> "December"
        }
    }
}