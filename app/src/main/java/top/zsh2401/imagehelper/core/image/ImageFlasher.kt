package top.zsh2401.imagehelper.core.image

import android.util.Log
import top.zsh2401.imagehelper.core.su.Su
import top.zsh2401.imagehelper.core.su.SuManager

/**
 * Created by zsh24 on 02/01/2018.
 */
class ImageFlasher(private val img:Images,private val srcPath:String):Runnable {
    lateinit var su: Su
    override fun run() {

        var path = ImageFinder.pathOf(img)
        Log.d("Flasher","src: $srcPath target: $path")
        su = SuManager.getSu()
        su.setCommandAndExecute("mv $srcPath $path")
        su.waitFor()
        Log.d("F",SuManager.execSuCommand("mv $srcPath $path").toString())
    }
    fun runAsync(callback:((Boolean)->Unit)?=null){
        Thread({
            try{
                run()
                callback?.invoke(su.process.exitValue() == 0)
            }catch (ex:Exception){
                callback?.invoke(false)
            }
        }).start()
    }
}