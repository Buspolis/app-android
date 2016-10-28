package kr.edcan.buspolis.util

import android.content.Context
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import es.dmoral.prefs.Prefs

/**
 * Created by LNTCS on 2016-10-25.
 */

object Utils{
    fun lang(context: Context) = Prefs.with(context).read("lang")!!

    var ServiceKey = "rOCJOizLiZuxSqugYyn9B10Np9YWY3+ohWdwZlgNR4dD+M1kNFUxAp9Zs++RJv1eKazIVCysjbm2P3ld1VWkvQ=="

    fun getNearStop(x: Double, y: Double, callback: AsyncHttpResponseHandler){
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

    fun convertCode(num: String) = if(num.length < 5){
        "0${num[0]}-${num.substring(1..3)}"
    }else{
        "${num.substring(0..1)}-${num.substring(2..4)}"
    }
}