package kr.edcan.buspolis.model

import android.view.View
import kr.edcan.buspolis.HelpActivity
import org.jetbrains.anko.startActivity

/**
 * Created by LNTCS on 2016-10-23.
 */
class BusStop{
    var name = MultiString()
    var code = ""
    var address = ""

    constructor(name: MultiString, code: String, address: String){
        this.name = name
        this.code = code
        this.address = address
    }

    var helpListener = View.OnClickListener{
        it.context.startActivity<HelpActivity>("name" to name.ko)
    }
}