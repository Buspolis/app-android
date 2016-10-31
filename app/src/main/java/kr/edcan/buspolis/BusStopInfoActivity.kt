package kr.edcan.buspolis

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.github.nitrico.lastadapter.LastAdapter
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_bus_stop_info.*
import kr.edcan.buspolis.databinding.ActivityBusStopInfoBinding
import kr.edcan.buspolis.model.BusStop
import kr.edcan.buspolis.model.ComingBus
import kr.edcan.buspolis.model.RM_Station
import kr.edcan.buspolis.util.HttpClient
import kr.edcan.buspolis.util.Utils
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import org.json.XML
import java.util.*
import kotlin.properties.Delegates

class BusStopInfoActivity : AppCompatActivity() {

    val arrayList = ArrayList<Any>()

    var station by Delegates.notNull<BusStop>()
    var realm by Delegates.notNull<Realm>()
    var binding by Delegates.notNull<ActivityBusStopInfoBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bus_stop_info)
        setData()
        setLayout()
    }

    private fun setLayout() {
        LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            busStopInfoRecyclerView.layoutManager = it
        }

        LastAdapter.with(arrayList, BR.item)
                .map<ComingBus>(R.layout.item_bus_stop_info)
                .onBind {
                    view.find<TextView>(R.id.busNumber).textColor =
                            ContextCompat.getColor(this@BusStopInfoActivity, Utils.backgroundColor((item as ComingBus).type))
                }
                .onClickListener(object : LastAdapter.OnClickListener {
                    override fun onClick(item: Any, view: View, type: Int, position: Int) {

                    }
                })
                .into(busStopInfoRecyclerView)
        refresh.setOnClickListener {
            if(!isInserting) setData()
        }
        busFare.setOnClickListener {
            startActivity<BusFareSelectActivity>()
        }
        busMap.setOnClickListener {

        }

        icBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    var isInserting = false
    private fun setData() {
        arrayList.clear()
        isInserting = true
        realm = Realm.getDefaultInstance()
        station = BusStop(this, realm.where(RM_Station::class.java).equalTo("id", intent.getIntExtra("id",0)).findFirst())
        binding.setVariable(BR.item, station)

        var params = RequestParams().apply {
            put("ServiceKey", Utils.ServiceKey)
            put("arsId", Utils.convertNum(station.code))
            put("numOfRows", 999)
            put("pageNo", 1)
        }
        HttpClient.get("/stationinfo/getStationByUid", params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = XML.toJSONObject(String(responseBody)).getJSONObject("ServiceResult")
                if (result.getJSONObject("msgHeader").getInt("headerCd") != 0) {
                    onFailure(statusCode, headers, responseBody, null)
                    return
                } else {
                    var list = result.getJSONObject("msgBody").getJSONArray("itemList")
                    arrayList.run {
                        for (i in 0..list.length() - 1) {
                            var obj = list.getJSONObject(i)
                            if(obj.getString("arrmsg1").contains("대기") || obj.getString("arrmsg1").contains("종료")) continue
                            add(ComingBus(
                                    obj.getInt("busRouteId"), obj.getString("rtNm"), obj.getInt("routeType"),
                                    getRemainTime(obj.getString("arrmsg1")),getRemainStat(obj.getString("arrmsg1"))
                            ))
                        }
                    }
                    arrayList.sortBy { (it as ComingBus).num }
                    busStopInfoRecyclerView.adapter.notifyDataSetChanged()
                }
                isInserting = false
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                //TODO 버스 찾을수 없음 나중에 ㅃㅃ
                isInserting = false
            }
        })
    }

    private fun  getRemainTime(string: String): String {
        if(string.contains("도착")) return "Soon"
        else{
            var min = string.split("분")[0]
            if (string.contains("초후")){
                var sec = string.split("분")[1].split("초")[0]
                return "${min}m ${sec}s"
            }else{
                return "${min}m"
            }
        }
    }

    private fun  getRemainStat(string: String): String {
        if(string.contains("도착")) return ""
        else{
            return string.split("[")[1].split("]")[0].split("번째")[0]
        }
    }
}
