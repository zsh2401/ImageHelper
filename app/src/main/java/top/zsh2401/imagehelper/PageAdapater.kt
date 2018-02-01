package top.zsh2401.imagehelper

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by zsh24 on 02/01/2018.
 */
class PageAdapater(private val viewList:ArrayList<View>): PagerAdapter() {
    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
         return view == `object`
    }

    override fun getCount(): Int{
        return viewList.size
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(viewList[position])
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        container?.addView(viewList[position])
        return viewList[position]
    }
}