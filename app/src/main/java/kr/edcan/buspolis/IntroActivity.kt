package kr.edcan.buspolis

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        Glide.with(this).load(R.drawable.bg_start).into(introBg)
        
    }
}
