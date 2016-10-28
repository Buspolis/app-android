package kr.edcan.buspolis

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.github.nitrico.lastadapter.LastAdapter
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_bus_info.*
import kr.edcan.buspolis.databinding.ActivityBusInfoBinding
import kr.edcan.buspolis.databinding.ContentBusinfoHeaderBinding
import kr.edcan.buspolis.databinding.ItemBusInfoBinding
import kr.edcan.buspolis.model.*
import kr.edcan.buspolis.util.HttpClient
import kr.edcan.buspolis.util.Utils
import org.jetbrains.anko.startActivity
import org.json.XML
import java.util.*
import kotlin.properties.Delegates

class BusInfoActivity : AppCompatActivity() {

    val arrayList = ArrayList<Any>()

    var realm by Delegates.notNull<Realm>()

    var route : String by Delegates.observable(" -> ") {
        prop, old, new ->
        busRoute.text = new
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBusInfoBinding>(this, R.layout.activity_bus_info)
        realm = Realm.getDefaultInstance()
        setData()
        setLayout()
    }

    private fun setLayout() {
        // TODO Bus Background Color, Bus Number, Bus Route
        headerBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.busGreen))
        var bus = realm.where(RM_Bus::class.java).equalTo("id", intent.getIntExtra("id",0)).findFirst()
        busNumber.text = bus.num
        findNameSet(bus.id)
        busInfo.setOnClickListener {
            startActivity<BusDetailActivity>()
        }
        busFare.setOnClickListener {
            // TODO start BusFare
        }
        icBack.setOnClickListener { finish() }

        LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            busInfoRecyclerView.layoutManager = it
        }
        LastAdapter.with(arrayList, BR.item)
                .map<BusStop>(R.layout.item_bus_info)
                .map<NearBusStop>(R.layout.content_businfo_near)
                .map<String>(R.layout.content_businfo_header)
                .onBindListener(object : LastAdapter.OnBindListener {
                    override fun onBind(item: Any, view: View, type: Int, position: Int) {
                        when (type) {
                            R.layout.item_bus_info -> {
                                val infoBinding = DataBindingUtil.getBinding<ItemBusInfoBinding>(view)
                                if (position == arrayList.size-1) {
                                    infoBinding.bottomIndicator.visibility = View.INVISIBLE
                                } else if(position == 3){
                                    infoBinding.topIndicator.visibility = View.INVISIBLE
                                }
                            }
                            R.layout.content_businfo_header -> {
                                val headerBinding = DataBindingUtil.getBinding<ContentBusinfoHeaderBinding>(view)
                                headerBinding.title.text = item.toString()
                            }
                        }
                    }
                })
                .into(busInfoRecyclerView)
    }

    private fun setData() {
        arrayList.run {
            add(getString(R.string.near_bus_stop))
            add(NearBusStop(MultiString(this@BusInfoActivity, "Gangnam Stn.", "江南站", "カンナム駅", "강남역"), "", 1))
            add(getString(R.string.bus_stop))
            add(BusStop(MultiString(this@BusInfoActivity, "Gangnam Stn.", "江南站", "カンナム駅", "강남역")))
            add(BusStop(MultiString(this@BusInfoActivity, "Gangnam Stn.", "江南站", "カンナム駅", "강남역")))
            add(BusStop(MultiString(this@BusInfoActivity, "Gangnam Stn.", "江南站", "カンナム駅", "강남역")))
            add(BusStop(MultiString(this@BusInfoActivity, "Gangnam Stn.", "江南站", "カンナム駅", "강남역")))
        }
    }

    fun findNameSet(id: Int){
        var params = RequestParams().apply {
            put("ServiceKey", Utils.ServiceKeyS)
            put("busRouteId", id)
        }
        Log.e("Asdf", "findName")
        HttpClient.get("/busRouteInfo/getStaionByRoute", params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = XML.toJSONObject(String(responseBody)).getJSONObject("ServiceResult")
                if(result.getJSONObject("msgHeader").getInt("headerCd") != 0){
                    onFailure(statusCode, headers, responseBody, null)
                    return
                }else{
                    var list = result.getJSONObject("msgBody").getJSONArray("itemList")
                    var itemS = list.getJSONObject(0)
                    var itemE = list.getJSONObject(list.length()-1)
                    var realm = Realm.getDefaultInstance()
                    var start = BusStop(this@BusInfoActivity, realm.where(RM_Station::class.java).equalTo("id", itemS.getInt("station")).findFirst())
                    var end = BusStop(this@BusInfoActivity, realm.where(RM_Station::class.java).equalTo("id", itemE.getInt("station")).findFirst())
                    route = "${start.name.getLocalName()} -> ${end.name.getLocalName()}"
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                //TODO 버스 찾을수 없음 나중에 ㅃㅃ
            }
        })
    }
}
