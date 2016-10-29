package kr.edcan.buspolis

import android.databinding.ObservableArrayList
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.github.nitrico.lastadapter.LastAdapter
import com.loopj.android.http.AsyncHttpResponseHandler
import com.yayandroid.locationmanager.LocationBaseActivity
import com.yayandroid.locationmanager.LocationConfiguration
import com.yayandroid.locationmanager.constants.ProviderType
import cz.msebera.android.httpclient.Header
import es.dmoral.prefs.Prefs
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kr.edcan.buspolis.model.BusStop
import kr.edcan.buspolis.model.MultiString
import kr.edcan.buspolis.model.RM_Station
import kr.edcan.buspolis.model.SearchItem
import kr.edcan.buspolis.util.Utils
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.json.XML

class MainActivity : LocationBaseActivity() {

    var sList = ObservableArrayList<Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLayout()
        getLocation()
    }

    private fun setLayout() {
        setSupportActionBar(toolbar)
        title = ""
        MultiString(this, "Where are you going?", "你去哪里?", "どこに行くの？").let {
            toolbarTitle.text = it.getLocalName()
            toolbarSubtitle.text = it.getEngSub()
        }
        sList.add(BusStop(MultiString(this, "Gangnam Stn.", "江南站", "カンナム駅", "강남역"), "01-023"))
//        sList.add(0) // cant find
        sList.add(SearchItem(100100409, "412"))
        sList.add(SearchItem(100100447, "7016"))

        LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            mainRecycler.layoutManager = it
        }
        LastAdapter.with(sList, BR.item)
                .map<SearchItem>(R.layout.item_search)
                .map<BusStop>(R.layout.content_main_header)
                .onBind {
                    if (item is BusStop){
                        view.find<TextView>(R.id.refreshNear).setOnClickListener {
                            getLocation()
                        }
                    }
                }
                .onClickListener(object : LastAdapter.OnClickListener {
                    override fun onClick(item: Any, view: View, type: Int, position: Int) {
                        if (position == 0) return
                        item as SearchItem
                        if(type == R.layout.item_search) startActivity<BusInfoActivity>("id" to item.id)
                    }
                })
                .into(mainRecycler)

        searchLay.setOnClickListener {
            startActivity<SearchActivity>()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.mainSettings -> {
                startActivity<SettingActivity>()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLocationChanged(location: Location) {
        Utils.getNearStop(location.longitude, location.latitude, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = XML.toJSONObject(String(responseBody)).getJSONObject("ServiceResult")
                if(result.getJSONObject("msgHeader").getInt("headerCd") != 0){
                    onFailure(statusCode, headers, responseBody, null)
                    return
                }else{
                    var item = result.getJSONObject("msgBody").getJSONArray("itemList").getJSONObject(0)
                    var station = Realm.getDefaultInstance().where(RM_Station::class.java).equalTo("id", item.getInt("stationId")).findFirst()
                    sList[0] = BusStop(this@MainActivity, station)
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
//                sList[0] = 0 TODO cant find nearby stop
            }
        })
        mainRecycler.adapter.notifyDataSetChanged()
    }

    override fun onLocationFailed(failType: Int) {
    }

    override fun getLocationConfiguration(): LocationConfiguration {
        return LocationConfiguration()
                .keepTracking(true)
                .askForGooglePlayServices(true)
                .setMinAccuracy(200.0f)
                .setTimeInterval((Prefs.with(this).readInt("autoRef", 3000) * 1000).toLong())
                .setWaitPeriod(ProviderType.GOOGLE_PLAY_SERVICES, 5 * 1000)
                .setWaitPeriod(ProviderType.GPS, 10 * 1000)
                .setWaitPeriod(ProviderType.NETWORK, 5 * 1000)
                .setGPSMessage("Would you mind to turn GPS on?")
                .setRationalMessage("Gimme the permission!")!!
    }
}
