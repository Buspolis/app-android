package kr.edcan.buspolis.model

import android.content.Context
import kotlin.properties.Delegates

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

}