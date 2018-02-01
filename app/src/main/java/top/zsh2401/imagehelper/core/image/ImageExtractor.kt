package top.zsh2401.imagehelper.core.image

import top.zsh2401.imagehelper.core.su.Su
import top.zsh2401.imagehelper.core.su.SuManager

/**
 * Created by zsh24 on 01/31/2018.
 */
class ImageExtractor(private val img:Images) :Runnable{
    private lateinit var su: Su
    override fun run(){
        var path = ImageFinder.pathOf(img)
        su = SuManager.getSu()
        su.setCommandAndExecute("cp $path /sdcard/${img.toString().toLowerCase()}.img")
        waitFor()
    }
    fun runAsync(callback:((Boolean)->Unit)? = null){
        Thread({
            try{
                run()
                callback?.invoke(su.process.exitValue() == 0)
            }catch (ex:Exception){
                callback?.invoke(false)
            }
        }).start()
    }
    fun cancel(){
        su.process.destroy()
    }
    fun waitFor():Int{
        return su.waitFor()
    }
}