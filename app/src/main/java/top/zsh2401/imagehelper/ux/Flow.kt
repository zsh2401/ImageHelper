package top.zsh2401.imagehelper.ux

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.support.design.widget.Snackbar
import android.view.View
import top.zsh2401.imagehelper.App
import top.zsh2401.imagehelper.R
import top.zsh2401.imagehelper.core.image.ImageExtractor
import top.zsh2401.imagehelper.core.image.Images
import top.zsh2401.imagehelper.core.su.SuManager

/**
 * Created by zsh24 on 02/01/2018.
 */
object Flow {
    lateinit var view:View
    lateinit var mainActivity: Activity
    fun extractRecovery(){
        extract(Images.Recovery)
    }
    fun extractBoot(){
       extract(Images.Boot)
    }
    private fun extract(img:Images){
        if(!rootCheck())return
        var bar = ProgressDialog(mainActivity)
        bar.setMessage(App.current.getString(R.string.msg_extracting))
        var extractor = ImageExtractor(img)
        bar.setOnDismissListener({
            extractor.cancel()
        })
        bar.show()
        extractor.runAsync({isSuccessful->
            mainActivity.runOnUiThread({
                if(bar.isShowing)
                    bar.dismiss()
                var builder = AlertDialog.Builder(mainActivity)
                if(isSuccessful){
                    builder.setTitle(R.string.title_successed)
                    builder.setMessage(R.string.msg_extract_ok)
                }else{
                    builder.setTitle(R.string.title_failed)
                    builder.setMessage(R.string.msg_extract_failed)
                }
                builder.setNegativeButton("ok",null)
                builder.show()
            })
        })
    }
    fun flashRecovery(){
        if(!rootCheck())return
    }
    fun flashBoot(){
        if(!rootCheck())return
    }
    fun rootCheck():Boolean{
        if(SuManager.haveRootPermission()){
            return true
        }else{
            Snackbar.make(view,"没有ROOT权限!",Snackbar.LENGTH_LONG)
                    .setAction("",null)
                    .show()
            return false
        }
    }
}