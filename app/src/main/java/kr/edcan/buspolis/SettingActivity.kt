package kr.edcan.buspolis

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import kr.edcan.buspolis.model.ListContent
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_setting.*
import kr.edcan.buspolis.databinding.ContentSettingsHeaderBinding
import kr.edcan.buspolis.databinding.ItemSettingsBinding
import org.jetbrains.anko.toast
import java.util.*

class SettingActivity : AppCompatActivity() {

    var arrayList: ArrayList<Any> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(resources.getColor(R.color.colorAccent))
        supportActionBar!!.title = "Settings"
        setLayout()
    }

    private fun setLayout() {
        LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            settingsRecycler.layoutManager = it
        }
        arrayList.add("General")
        arrayList.add(ListContent("Auto Refresh", "Set Auto Refresh Time", "30s"))
        arrayList.add(ListContent("Auto Refresh", "Set Auto Refresh Time", "30s"))
        arrayList.add(ListContent("Auto Refresh", "Set Auto Refresh Time", "30s"))
        arrayList.add(ListContent("Auto Refresh", "Set Auto Refresh Time", "30s"))
        arrayList.add(ListContent("Auto Refresh", "", "30s"))
        LastAdapter.with(arrayList, BR.item)
                .onBindListener(object: LastAdapter.OnBindListener{
                    override fun onBind(item: Any, view: View, type: Int, position: Int) {
                        when(type){
                            R.layout.content_settings_header -> {
                                val binding = DataBindingUtil.getBinding<ContentSettingsHeaderBinding>(view)
                                binding.title.text = arrayList[position].toString()
                            }
                            R.layout.item_settings -> {
                                val contentBinding = DataBindingUtil.getBinding<ItemSettingsBinding>(view)
                                item as ListContent
                                if(item.content.equals("")) contentBinding.content.visibility = View.GONE
                            }
                        }
                    }
                })
                .onClickListener(object: LastAdapter.OnClickListener{
                    override fun onClick(item: Any, view: View, type: Int, position: Int) {
                        toast(position.toString())
                    }
                })
                .map<String>(R.layout.content_settings_header)
                .map<ListContent>(R.layout.item_settings)
                .into(settingsRecycler)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
