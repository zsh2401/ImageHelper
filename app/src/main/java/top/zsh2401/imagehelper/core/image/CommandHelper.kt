package top.zsh2401.imagehelper.core.image

/**
 * Created by zsh24 on 02/01/2018.
 */
object CommandHelper {
    fun haveFindCommand():Boolean{
        try{
            Runtime.getRuntime().exec("find").destroy()
            return true
        }catch (ex:Exception){
            ex.printStackTrace()
            return false
        }
    }
}