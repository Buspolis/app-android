package kr.edcan.buspolis.model

/**
 * Created by LNTCS on 2016-10-23.
 */

class SearchItem {
    var keyword = ""
    var option : Any? = null

    constructor(keyword: String) {
        this.keyword = keyword
    }
}
