package top.zsh2401.imagehelper.ux

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import top.zsh2401.imagehelper.App
import top.zsh2401.imagehelper.R
import top.zsh2401.imagehelper.core.image.ImageExtractor
import top.zsh2401.imagehelper.core.image.ImageFlasher
import top.zsh2401.imagehelper.core.image.Images
import top.zsh2401.imagehelper.core.rebootToRecovery
import top.zsh2401.imagehelper.core.su.SuManager
import java.io.File
import java.net.URI

/**
 * Created by zsh24 on 02/01/2018.
 */
object Flow {
    val FILE_SELECT_REQUEST_CODE = 2401
    val TAG = "Flow"
    lateinit var view:View
    lateinit var mainActivity: MainActivity
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
                builder.setTitle(
                        if(isSuccessful)R.string.title_successed
                        else R.string.title_failed)
                builder.setMessage(
                        if(isSuccessful)R.string.msg_extract_ok
                        else R.string.msg_extract_failed)
                builder.setNegativeButton("ok",null)
                builder.show()
            })
        })
    }
    fun flashRecovery(){
        flash(Images.Recovery)
    }
    fun flashBoot(){
        flash(Images.Boot)
    }
    private fun flash(img:Images){
        if(!rootCheck())return
        selectAFile()
        mainActivity.onFileSelectedCallback = {path->
            flash(path,img)
        }
    }
    private fun flash(pathUri: Uri, img: Images){
        var bar = ProgressDialog(mainActivity)
        bar.setMessage(App.current.getString(R.string.msg_extracting))
        var flasher = ImageFlasher(img,pathUri)
        bar.setOnDismissListener({
            flasher.su.process.destroy()
        })
        flasher.runAsync({isSuccessful->
            var dialog = AlertDialog.Builder(mainActivity)
            dialog.setTitle(
                    if(isSuccessful) R.string.title_successed
                    else R.string.title_failed)
            dialog.setMessage(
                    if(isSuccessful)R.string.msg_flash_ok
                    else R.string.msg_flash_failed)
            if(isSuccessful && img == Images.Recovery){
                dialog.setPositiveButton(R.string.reboot_to_recovery,{_,_->
                    rebootToRecovery()
                })
                dialog.setNegativeButton(R.string.donot_reboot,null)
            }else{
                dialog.setPositiveButton("ok",null)
            }

            mainActivity.runOnUiThread({
                bar.dismiss()
                dialog.show()
            })
        })
        bar.show()
    }
    private fun selectAFile(){
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        mainActivity.startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                FILE_SELECT_REQUEST_CODE)
    }
    private fun rootCheck():Boolean{
        if(SuManager.haveRootPermission()){
            return true
        }else{
            Snackbar.make(view,R.string.warning_no_root,Snackbar.LENGTH_LONG)
                    .setAction("ok",null)
                    .show()
            return false
        }
    }
}