package kr.edcan.buspolis.model

import kotlin.properties.Delegates

/**
 * Created by LNTCS on 2016-10-23.
 */

class SearchItem {
    var type by Delegates.notNull<listType>()
    var id = ""
    var keyword = ""
    var option: Any? = null

    constructor(keyword: String) {
        this.keyword = keyword
    }

    enum class listType {
        BUS, BUSSTOP
    }
}
