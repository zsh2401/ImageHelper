package top.zsh2401.imagehelper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

class MainActivity : AppCompatActivity(),
        View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener
{


    private lateinit var mNavView:NavigationView
    private lateinit var mToolbar:Toolbar
    private lateinit var mDrawer:DrawerLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var mBottomNav:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewObj()
        initViewPager()
        initDrawer()
        initEvent()
    }
    private  lateinit var viewList:ArrayList<View>
    private fun initEvent(){
        mBottomNav.setOnNavigationItemSelectedListener(this)
        mViewPager.addOnPageChangeListener(this)
        initNavEvent()
    }
    private fun initNavEvent(){
        mNavView.setNavigationItemSelectedListener({view->
            when(view.itemId){
                R.id.nav_about->{
                    mDrawer.closeDrawers()
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.title_about)
                    builder.setMessage(R.string.msg_about)
                    builder.setNegativeButton(R.string.btn_ok,null)
                    builder.show()
                }
                R.id.nav_donate->{
                    mDrawer.closeDrawers()
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.title_donate)
                    builder.setMessage(R.string.msg_donate)
                    builder.setPositiveButton(R.string.btn_cp_wechat_account,{ _, _ ->
                        copyWechatAccount()
                        Snackbar.make(mNavView, R.string.msg_copied, Snackbar.LENGTH_SHORT)
                                .setAction("ok", null).show()
                    })
                    builder.setNegativeButton(R.string.btn_cp_alipay_redpacket_code,{ _, _ ->
                        copyAlipayRedpacketCode()
                        Snackbar.make(mNavView, R.string.msg_copied, Snackbar.LENGTH_SHORT)
                                .setAction("ok", null).show()
                    })
                    builder.setNeutralButton(R.string.btn_goto_alipay,{ _, _ ->
                        if(!gotoAlipay()){
                            copyAlipayAccount()
                            Snackbar.make(mNavView,R.string.msg_goto_alipay_failed,Snackbar.LENGTH_LONG)
                                    .setAction("ok",null)
                                    .show()
                        }
                    })
                    builder.show()
                }
                R.id.nav_opensource->{
                    mDrawer.closeDrawers()
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.title_opensource)
                    builder.setMessage(R.string.msg_opensouce)
                    builder.setPositiveButton(R.string.btn_viewongithub,{v,i->
                        var intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.url_opensource)))
                        startActivity(intent)
                    })
                    builder.show()
                }
            }
            mDrawer.closeDrawers()
            true
        })
    }
    private fun initViewPager(){
        viewList = arrayListOf()
        viewList.add(getLayoutInflater().inflate(R.layout.item_view_page_flash, null))
        viewList.add(getLayoutInflater().inflate(R.layout.item_view_page_extract, null))
        mViewPager.adapter = PageAdapater(viewList)
    }

    private fun initViewObj(){
        mNavView = findViewById(R.id.nav_view)
        mViewPager = findViewById(R.id.view_page_main)
        mBottomNav = findViewById(R.id.bottomNavigationView)
        mDrawer = findViewById(R.id.drawer_layout)
        mToolbar = findViewById(R.id.toolbar_main)

    }
    private fun initDrawer(){
        setSupportActionBar(mToolbar)
        var toggle = ActionBarDrawerToggle(this,
                mDrawer,mToolbar,
                R.string.nav_drawer_open,R.string.nav_drawer_close)
        mDrawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onClick(p0: View?) {

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.btm_nav_flash->{
                mViewPager.setCurrentItem(0)
                return true}
            R.id.btm_nav_extract->{
                mViewPager.setCurrentItem(1)
                return true}
            else->false
        }
    }
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        when(position){
            0->{mBottomNav.selectedItemId = R.id.btm_nav_flash}
            1->{mBottomNav.selectedItemId = R.id.btm_nav_extract}
        }
    }
}
