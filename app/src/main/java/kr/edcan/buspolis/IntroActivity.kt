package kr.edcan.buspolis

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import es.dmoral.prefs.Prefs
import kotlinx.android.synthetic.main.activity_intro.*
import kr.edcan.u_stream.adapter.IntroPagerAdapter
import org.jetbrains.anko.textColor

class IntroActivity : AppCompatActivity() {

    var chkedNum = 0
    var isCovered = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_intro)

        Glide.with(this).load(R.drawable.bg_start).into(introBg)

        introPager.adapter = IntroPagerAdapter(this)

        var langs = arrayOf(langEng, langCn, langJp)
        langs.forEachIndexed { i, view ->
            view.setOnClickListener {
                if(!isCovered) return@setOnClickListener
                chkedNum = i
                for (j in 0..langs.size - 1) {
                    if (i == j) {
                        langs[j].setBackgroundResource(R.drawable.selector_fill)
                        langs[j].textColor = Color.BLACK
                    } else {
                        langs[j].setBackgroundResource(R.drawable.selector_border)
                        langs[j].textColor = Color.WHITE
                    }
                }
            }
        }

        introNext.setOnClickListener {
            if(isCovered) {
                isCovered = false
                Prefs.with(this).write("lang", arrayOf("en","cn","jp")[chkedNum])
                introCover.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
                introNext.textColor = ContextCompat.getColor(this, R.color.colorAccent)
            }else{
                if(introPager.currentItem == 1){
                    introNext.text = getString(R.string.set_permission)
                }else if(introPager.currentItem == 2){
                    //TODO 퍼미션
                }
                introPager.setCurrentItem(introPager.currentItem+1, true)
            }
        }
    }
}
