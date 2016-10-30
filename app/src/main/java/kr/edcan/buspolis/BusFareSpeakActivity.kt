package kr.edcan.buspolis

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bus_fare_speak.*
import kr.edcan.buspolis.util.KoreanRomanizer

class BusFareSpeakActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_fare_speak)
        helpKor.text = "${intent.getStringExtra("name")} 버스 정류장은 어디에 있나요?"
        helpRomaji.text = KoreanRomanizer.romanize(helpKor.text.toString())
        icBack.setOnClickListener { finish() }
    }
}
