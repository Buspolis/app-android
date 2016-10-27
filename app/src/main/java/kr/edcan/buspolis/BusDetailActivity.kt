package kr.edcan.buspolis

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_bus_detail.*
import kr.edcan.buspolis.databinding.ActivityBusDetailBinding

class BusDetailActivity : AppCompatActivity() {

    var color = "#54A953"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBusDetailBinding>(this, R.layout.activity_bus_detail)
        setLayout()
        setData()
    }

    private fun setLayout() {
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        title = getString(R.string.information)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setData() {
        busRoute.text = "구산동 -> 노량진"
        busRouteContent.text = "Sunjun Traffic Service"
        // TODO Bus Service Hour Text
//        busServiceHour.text = Html.fromHtml(
//        )
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
