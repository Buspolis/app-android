package kr.edcan.buspolis.util

import android.content.Context
import es.dmoral.prefs.Prefs

/**
 * Created by LNTCS on 2016-10-25.
 */

object Utils{
    fun lang(context: Context) = Prefs.with(context).read("lang")!!

    fun getNearStop(){
        
    }
}