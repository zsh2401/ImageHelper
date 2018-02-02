package top.zsh2401.imagehelper.core

import top.zsh2401.imagehelper.core.su.SuManager

/**
 * Created by zsh24 on 02/02/2018.
 */
fun rebootToRecovery(){
    SuManager.getSu().setCommandAndExecute("reboot recovery")
}
fun reboot(){
    SuManager.getSu().setCommandAndExecute("reboot")
}