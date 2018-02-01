package top.zsh2401.imagehelper.core

/**
 * Created by zsh24 on 01/31/2018.
 */
class ImageExtractor(private val img:Images,private val savePath:String):Runnable {

    override fun run() {
        var su = Runtime.getRuntime().exec("su")
        su.outputStream.write("cp ".toByteArray())
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val TAG = "ImageExtractor"

}