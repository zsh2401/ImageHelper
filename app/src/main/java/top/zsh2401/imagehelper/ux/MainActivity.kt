package top.zsh2401.imagehelper.ux

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import top.zsh2401.imagehelper.App
import top.zsh2401.imagehelper.R
import java.net.URI

class MainActivity : AppCompatActivity(),
        View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener
{

    private val TAG = "MainActivity"
    private lateinit var viewList:ArrayList<View>
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
        Flow.mainActivity = this
        Flow.view = mNavView
        initEvent()
        checkAndRequestPermission()
    }
    private val myPermissionRequestCode = 2405
    private fun checkAndRequestPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)return
        var permissions = arrayOf(
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE")
        var allGrated =checkPermissionAllGranted(permissions)
        if(allGrated)return
        ActivityCompat.requestPermissions(this, permissions,myPermissionRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==myPermissionRequestCode){
            var isAllGranted = true
            for(grant in grantResults){
                if(grant != PackageManager.PERMISSION_GRANTED){
                    isAllGranted = false
                    break
                }
            }

            if(!isAllGranted){
                var builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.title_sorry)
                builder.setMessage(R.string.msg_permission_deined)
                builder.setOnDismissListener({
                    finish()
                })
                builder.setNegativeButton("ok",null)
//                builder.setNeutralButton("ok",null)
                builder.show()
            }
        }
    }

    private fun checkPermissionAllGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false
            }
        }
        return true
    }

    private fun initEvent(){
        mBottomNav.setOnNavigationItemSelectedListener(this)
        mViewPager.addOnPageChangeListener(this)
        initNavEvent()
        viewList[0].findViewById<Button>(R.id.btn_flash_recovery).setOnClickListener(this)
        viewList[0].findViewById<Button>(R.id.btn_flash_boot).setOnClickListener(this)
        viewList[1].findViewById<Button>(R.id.btn_extract_recovery).setOnClickListener(this)
        viewList[1].findViewById<Button>(R.id.btn_extract_boot).setOnClickListener(this)
    }

    var onFileSelectedCallback:((Uri)->Unit)? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Flow.FILE_SELECT_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK){
            Log.d(TAG,"real path: " + App.current.getRealPathFromURI(data!!.data))
            onFileSelectedCallback?.invoke(data!!.data)
        }
    }

    private fun initNavEvent(){
        mNavView.setNavigationItemSelectedListener({view->
            when(view.itemId){
                R.id.nav_about ->{
                    mDrawer.closeDrawers()
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.title_about)
                    builder.setMessage(R.string.msg_about)
                    builder.setNegativeButton(R.string.btn_ok,null)
                    builder.show()
                }
                R.id.nav_donate ->{
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
                            Snackbar.make(mNavView, R.string.msg_goto_alipay_failed,Snackbar.LENGTH_LONG)
                                    .setAction("ok",null)
                                    .show()
                        }
                    })
                    builder.show()
                }
                R.id.nav_opensource ->{
                    mDrawer.closeDrawers()
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.title_opensource)
                    builder.setMessage(R.string.msg_opensouce)
                    builder.setPositiveButton(R.string.btn_viewongithub,{ v, i->
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
                R.string.nav_drawer_open, R.string.nav_drawer_close)
        mDrawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btn_extract_boot->{
                Flow.extractBoot()
            }
            R.id.btn_extract_recovery->{
                Flow.extractRecovery()
            }
            R.id.btn_flash_boot->{
                Flow.flashBoot()
            }
            R.id.btn_flash_recovery->{
                Flow.flashRecovery()
            }
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.btm_nav_flash ->{
                mViewPager.setCurrentItem(0)
                return true}
            R.id.btm_nav_extract ->{
                mViewPager.setCurrentItem(1)
                return true}
            else->false
        }
    }
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        when(position){
            0->{mBottomNav.selectedItemId = R.id.btm_nav_flash
            }
            1->{mBottomNav.selectedItemId = R.id.btm_nav_extract
            }
        }
    }
}
