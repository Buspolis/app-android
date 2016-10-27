package kr.edcan.buspolis

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_bus_info.*
import kr.edcan.buspolis.databinding.ActivityBusInfoBinding
import kr.edcan.buspolis.databinding.ContentBusinfoHeaderBinding
import kr.edcan.buspolis.databinding.ItemBusInfoBinding
import kr.edcan.buspolis.model.BusStop
import kr.edcan.buspolis.model.MultiString
import kr.edcan.buspolis.model.NearBusStop
import java.util.*

class BusInfoActivity : AppCompatActivity() {

    val arrayList = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBusInfoBinding>(this, R.layout.activity_bus_info)
        setData()
        setLayout()
    }

    private fun setLayout() {
        // TODO Bus Background Color, Bus Number, Bus Route
        headerBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.busGreen))
        busNumber.text = "752"
        busRoute.text = "구산동 -> 총신대"
        busInfo.setOnClickListener {
            // TODO start BusInfo
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
}
