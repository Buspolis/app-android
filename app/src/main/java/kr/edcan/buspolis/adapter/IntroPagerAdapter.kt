package kr.edcan.u_stream.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import kr.edcan.buspolis.R
import org.jetbrains.anko.find

/**
 * Created by LNTCS on 2016-03-11.
 */
class IntroPagerAdapter(internal var mContext: Context) : PagerAdapter() {

    private val mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(pager: View?, position: Int): Any {
        val v = mInflater.inflate(R.layout.content_intro_pager, null)
        var imgs = arrayOf(R.drawable.img_tutorial1, R.drawable.img_tutorial2, R.drawable.img_tutorial3)

        Glide.with(mContext).load(imgs[position]).into(v.find<ImageView>(R.id.introImg))

        (pager as ViewPager).addView(v, null)
        return v
    }

    override fun destroyItem(pager: View?, position: Int, view: Any?) {
        (pager as ViewPager).removeView(view as View?)
    }

    override fun isViewFromObject(v: View, obj: Any): Boolean {
        return v === obj
    }
}