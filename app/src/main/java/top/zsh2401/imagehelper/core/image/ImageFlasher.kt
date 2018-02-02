package top.zsh2401.imagehelper.core.image

import android.media.Image
import android.net.Uri
import android.util.Log
import top.zsh2401.imagehelper.App
import top.zsh2401.imagehelper.core.su.Su
import top.zsh2401.imagehelper.core.su.SuManager
import top.zsh2401.imagehelper.ux.FileUtil
import top.zsh2401.imagehelper.ux.Flow
import java.io.File
import java.net.URI

/**
 * Created by zsh24 on 02/01/2018.
 */
class ImageFlasher(private val img:Images,private val fileUri:Uri):Runnable {
    private val TAG = "Image Flasher"
    lateinit var su: Su
    override fun run() {
        Log.d(TAG,"the uri: " + fileUri)
        var tempPath =getTempFile(fileUri)
        var targetPath = ImageFinder.pathOf(img)
        su = SuManager.getSu()
        su.setCommandAndExecute("mv $tempPath $targetPath")
        su.waitFor()
    }
    fun runAsync(callback:((Boolean)->Unit)?=null){
        Thread({
            try{
                run()
                callback?.invoke(su.process.exitValue() == 0)
            }catch (ex:Exception){
                ex.printStackTrace()
                callback?.invoke(false)
            }
        }).start()
    }
}