package kr.edcan.buspolis.model

import android.content.Context
import android.view.View
import kr.edcan.buspolis.BusStopInfoActivity
import kr.edcan.buspolis.HelpActivity
import kr.edcan.buspolis.util.Utils
import org.jetbrains.anko.startActivity

/**
 * Created by LNTCS on 2016-10-23.
 */
class BusStop{
    var id = 0
    var name = MultiString()
    var code = ""

    constructor(name: MultiString, code: String){
        this.name = name
        this.code = code
    }

    constructor(name: MultiString) {
        this.name = name
    }

    var helpListener = View.OnClickListener{
        it.context.startActivity<HelpActivity>("name" to name.ko, "id" to id)
    }

    var infoListener = View.OnClickListener{
        it.context.startActivity<BusStopInfoActivity>("name" to name.ko, "id" to id)
    }

    constructor(context: Context, station: RM_Station){
        this.id = station.id
        this.name = MultiString(context, station.name_en, station.name_cn, station.name_jp, station.name)
        this.code = Utils.convertCode(station.num)
    }
}