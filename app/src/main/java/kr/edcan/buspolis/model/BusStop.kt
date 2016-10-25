package kr.edcan.buspolis.model

/**
 * Created by LNTCS on 2016-10-23.
 */
class BusStop{
    var name = mapOf("ko" to "", "en" to "", "jp" to "", "zh" to "")
    var code = ""
    var address = ""

    constructor(name: Map<String, String>, code: String, address: String){
        this.name = name
        this.code = code
        this.address = address
    }

    fun getName(lang: String) : String{
        return name[lang] as String
    }
    fun getLocalName() = name["ko"] //TODO 자신이 설정한 주 언어 구분자
}