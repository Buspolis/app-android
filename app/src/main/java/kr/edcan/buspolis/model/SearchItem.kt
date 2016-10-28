package kr.edcan.buspolis.model

import kotlin.properties.Delegates

/**
 * Created by LNTCS on 2016-10-23.
 */

class SearchItem {
    var type by Delegates.notNull<listType>()
    var id = 0
    var keyword = ""
    var option: Any? = null

    constructor(keyword: String) {
        this.keyword = keyword
    }

    constructor(id: Int, keyword: String) {
        this.id = id
        this.keyword = keyword
    }

    enum class listType {
        BUS, BUSSTOP
    }
}
