package cz.mendelu.xmusil5.plantdiscoverer

import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class UtilsTest {

    private var testDateMillis = 1668034800000
    private var testDateString = "2022/11/10"

    @Test
    fun dateUtils_stringFormatFromMillis(){
        val dateString = DateUtils.getDateString(testDateMillis)
        assertEquals(dateString, testDateString)
    }

    @Test
    fun dateUtils_daysBetween(){
        val date1 = Calendar.getInstance().apply { set(2022, 1, 1, 10, 0, 0) }
        val date2 = Calendar.getInstance().apply{ set(2022, 1, 6, 10, 0, 0) }

        val daysDifference = DateUtils.daysBetween(date1, date2)
        assertEquals(daysDifference, 5)
    }

    @Test
    fun dateUtils_getDate(){
        val fromUtils = DateUtils.getDate(testDateMillis)
        assertEquals(2022, fromUtils.get(Calendar.YEAR))
        assertEquals(11, fromUtils.get(Calendar.MONTH)+1)
        assertEquals(10, fromUtils.get(Calendar.DATE))
    }

    @Test
    fun dateUtils_millisFromStringFormat(){
        val dateMillis = DateUtils.getUnixTime(2022, 11, 10)
        assertEquals(dateMillis, testDateMillis)
    }

}