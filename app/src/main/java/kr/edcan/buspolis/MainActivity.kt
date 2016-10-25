package kr.edcan.buspolis

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kr.edcan.buspolis.model.BusStop
import kr.edcan.buspolis.model.MultiString
import kr.edcan.buspolis.model.SearchItem
import org.jetbrains.anko.toast
import java.util.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    var sList = ArrayList<Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLayout()
//     TODO 영어일 때 기존에 영어 있던 부분 지우는 함수
        hideLayout()
    }

    private fun hideLayout() {

    }

    private fun setLayout() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        sList.add(BusStop(MultiString(this, "Gangnam Stn.", "江南站", "カンナム駅", "강남역"), "01-023", "내 마음속"))
        sList.add(SearchItem("0"))
        sList.add(SearchItem("1"))
        sList.add(SearchItem("2"))
        sList.add(SearchItem("3"))
        sList.add(SearchItem("4"))
        sList.add(SearchItem("5"))
        sList.add(SearchItem("6"))
        sList.add(SearchItem("7"))
        sList.add(SearchItem("9"))
        sList.add(SearchItem("10"))
        sList.add(SearchItem("11"))
        sList.add(SearchItem("12"))
        sList.add(SearchItem("13"))

        LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            mainRecycler.layoutManager = it
        }

        LastAdapter.with(sList, BR.item)
                .map<SearchItem>(R.layout.item_search)
                .map<BusStop>(R.layout.content_main_header)
                .onClickListener(object : LastAdapter.OnClickListener {
                    override fun onClick(item: Any, view: View, type: Int, position: Int) {
                        if (position == 0) return
                        item as SearchItem
                        toast(item.keyword)
                    }
                })
                .into(mainRecycler)
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
}
