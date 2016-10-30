package kr.edcan.buspolis.util

import android.content.Context
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import es.dmoral.prefs.Prefs
import kr.edcan.buspolis.R

/**
 * Created by LNTCS on 2016-10-25.
 */

object Utils {
    fun lang(context: Context) = Prefs.with(context).read("lang")!!

    var ServiceKey = "rOCJOizLiZuxSqugYyn9B10Np9YWY3+ohWdwZlgNR4dD+M1kNFUxAp9Zs++RJv1eKazIVCysjbm2P3ld1VWkvQ=="

    var ServiceKeyS = "C49c3xuCEXZMaWm1Z7SKQQyGM6xv10cLJK+cJPaNmWleqJl8iCn/FJ/ky5z9O73A9+9Y2Y3+AYDIc0dix/MZYA=="

    fun getNearStop(x: Double, y: Double, callback: AsyncHttpResponseHandler) {
        var params = RequestParams().apply {
            put("ServiceKey", ServiceKey)
            put("tmX", x)
            put("tmY", y)
            put("radius", 500)
            put("numOfRows", 999)
            put("pageNo", 1)
        }
        HttpClient.get("/stationinfo/getStationByPos", params, callback)
    }

    fun convertCode(num: String) = if (num.length < 5) {
        "0${num[0]}-${num.substring(1..3)}"
    } else {
        "${num.substring(0..1)}-${num.substring(2..4)}"
    }

    fun backgroundColor(position: Int): Int {
        val backgroundArr = arrayOf(R.color.busBlue, R.color.busRed, R.color.busGreen, R.color.busBlue, R.color.busGreen, R.color.busYellow, R.color.busRed)
        val realPosition = if (position > backgroundArr.size) 6 else position
        return backgroundArr[realPosition]
    }

    fun titleBarColor(position: Int): Int {
        val titleBarArr = arrayOf(R.color.busBlueDark, R.color.busRedDark, R.color.busGreenDark, R.color.busBlueDark, R.color.busGreenDark, R.color.busYellowDark, R.color.busRedDark)
        val realPosition = if (position > titleBarArr.size) 6 else position
        return titleBarArr[realPosition]
    }

    fun busFareColor(position: Int): Int {
        val colorArr = arrayOf(R.color.busBlue, R.color.busGreen, R.color.busYellow)
        return colorArr[if (position > colorArr.size) 0 else position]
    }

    fun busFareColorDark(position: Int): Int {
        val colorArr = arrayOf(R.color.busBlueDark, R.color.busGreenDark, R.color.busYellowDark)
        return colorArr[if (position > colorArr.size) 0 else position]
    }

    fun busFare(payment: Int, busType: Int, adult: Int, youth: Int, child: Int): Int {
        var result = 0
        when (payment) {
            0 -> {
                when (busType) {
                    0, 1 -> {
                        result += (1200 * adult + 720 * youth + 450 * child)
                    }
                    2 -> {
                        result += (1100 * adult + 560 * youth + 350 * child)
                    }
                }
            }
            1 -> {
                when (busType) {
                    0, 1 -> {
                        result += (1300 * adult + 1300 * youth + 450 * child)
                    }
                    2 -> {
                        result += (1200 * adult + 1200 * youth + 350 * child)
                    }
                }
            }
        }
        return result
    }
}