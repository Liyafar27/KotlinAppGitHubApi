package ru.example.myfirstkotlinapp.utils

import ru.example.myfirstkotlinapp.model.User
import java.text.SimpleDateFormat
import java.util.*

fun Date.removeTime(): Date {
    val cal = Calendar.getInstance() // get calendar instance
    cal.time = this
//    val  sdf =  SimpleDateFormat("dd/MM/yyyy")
    cal[Calendar.DAY_OF_MONTH] = 1
    cal[Calendar.HOUR_OF_DAY] = 0 // set hour to midnight
    cal[Calendar.MINUTE] = 0 // set minute in hour
    cal[Calendar.SECOND] = 0 // set second in minute
    cal[Calendar.MILLISECOND] = 0 // set millisecond in second

    return cal.time
//    sdf.format(

}

private fun calendarsObjc1(yearStr: Int): Date {
    val c1 = Calendar.getInstance()
    c1[Calendar.DAY_OF_MONTH] = 31
    c1[Calendar.MONTH] = 11
    c1[Calendar.YEAR] = yearStr - 1
    c1[Calendar.HOUR_OF_DAY] = 23
    c1[Calendar.MINUTE] = 59
    c1[Calendar.SECOND] = 59
    c1[Calendar.MILLISECOND] = 0

    return c1.time
}

fun calendarsObjc2(yearStr: Int): Date {
    val c2 = Calendar.getInstance()
    c2[Calendar.DAY_OF_MONTH] = 1
    c2[Calendar.MONTH] = 0
    c2[Calendar.YEAR] = yearStr + 1
    c2[Calendar.HOUR_OF_DAY] = 0
    c2[Calendar.MINUTE] = 0
    c2[Calendar.SECOND] = 0
    c2[Calendar.MILLISECOND] = 0

    return c2.time
}
fun monthObjc(eInt: Int): Date {
    val m = Calendar.getInstance()
    m[Calendar.DAY_OF_MONTH] = 1
    m[Calendar.MONTH] = 0 + eInt
    m[Calendar.YEAR] = 0
    m[Calendar.HOUR_OF_DAY] = 0
    m[Calendar.MINUTE] = 0
    m[Calendar.SECOND] = 0
    m[Calendar.MILLISECOND] = 0

    return m.time
}


fun yearBetween(arrayMapDate: Map<Date, Int>, yearStr: Int): Map<Date, Int> {

    return arrayMapDate.filter {
        it.key.after(calendarsObjc1(yearStr))
    }.filter {
        it.key.before(calendarsObjc2(yearStr))
    }
}

fun yearBetween2(arrayAnyDate: Map<User, Date>, yearStr: Int): Map<User, Date> {

    return arrayAnyDate.filter {
        it.value.after(calendarsObjc1(yearStr))
    }.filter {
        it.value.before(calendarsObjc2(yearStr))
    }
}
fun certainMonth(arrayIntent: Map<User, Date>, eInt: Int): Map<User, Date> {
    val  sdf =  SimpleDateFormat("mm")
    val gdi  =

//    sdf.format(arrayIntent.values)
//    sdf.format(monthObjc(eInt))
    return arrayIntent.filter {(sdf.format(it.value.time) == sdf.format(monthObjc(eInt)))}

//    return arrayIntent.filter { it.value.month.equals(monthObjc(eInt).month) }

}


object MonthObj {
    val month = listOf(
        " Jan",
        " Feb",
        " Mar",
        " Apr",
        " May",
        " Jun",
        " Jul",
        " Aug",
        " Sep",
        " Oct",
        " Nov",
        " Dec"
    )
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Date()
}


