package top.zsh2401.imagehelper.core.su

/**
 * Created by zsh24 on 02/01/2018.
 */
data class ExecResult(val output:String,val exitValue:Int){
    override fun toString(): String {
        return output
    }
}