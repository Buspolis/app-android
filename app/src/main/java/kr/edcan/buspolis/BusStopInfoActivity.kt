package kr.edcan.buspolis

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_bus_stop_info.*
import kr.edcan.buspolis.databinding.ContentBusinfoHeaderBinding
import kr.edcan.buspolis.databinding.ItemBusStopInfoBinding
import kr.edcan.buspolis.model.BusStop
import org.jetbrains.anko.startActivity
import java.util.*

class BusStopInfoActivity : AppCompatActivity() {

    val arrayList = ArrayList<Any>()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_stop_info)
        setData()
        setLayout()
    }

    private fun setLayout() {
        LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            busStopInfoRecyclerView.layoutManager = it
        }

        LastAdapter.with(arrayList, BR.item)
                .map<BusStop>(R.layout.item_bus_stop_info)
                .map<String>(R.layout.content_businfo_header)
                .onBindListener(object : LastAdapter.OnBindListener {
                    override fun onBind(item: Any, view: View, type: Int, position: Int) {
                        when (type) {
                            R.layout.item_bus_stop_info -> {
                                val infoBinding = DataBindingUtil.getBinding<ItemBusStopInfoBinding>(view)
                            }

                            R.layout.content_businfo_header -> {
                                val headerBinding = DataBindingUtil.getBinding<ContentBusinfoHeaderBinding>(view)
                                headerBinding.title.text = item.toString()
                            }
                        }
                    }
                })
                .onClickListener(object : LastAdapter.OnClickListener {
                    override fun onClick(item: Any, view: View, type: Int, position: Int) {
                        startActivity<BusStopInfoActivity>()
                    }
                })
                .into(busStopInfoRecyclerView)
        refresh.setOnClickListener {

        }
        busFare.setOnClickListener {

        }
        busMap.setOnClickListener {

        }
    }

    private fun setData() {
        arrayList.run {
//            add(getString(R.string.seoul_blue_bus))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
