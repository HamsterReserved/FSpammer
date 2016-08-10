package space.hamsters.fspammer;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by hamster on 16/8/10.
 * <p/>
 * Register the hook in Xposed.
 */
public class XposedHookRegister implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.tencent.mobileqq")) {
            TroopMessageHook.hookBaseMessageProcessorForTroop(loadPackageParam.classLoader);
        }
    }
}
