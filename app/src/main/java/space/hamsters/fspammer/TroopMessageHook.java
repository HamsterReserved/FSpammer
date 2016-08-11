package space.hamsters.fspammer;

import java.lang.reflect.Field;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by hamster on 16/8/10.
 * <p/>
 * Hook for the spammers!
 */
public class TroopMessageHook {
    private static XSharedPreferences mPreferences;
    private static Class<?> MsgClass;
    private static Class<?> PBDecodeContextClass;
    private static Class<?> MessageInfoClass;
    private static Class<?> MessageRecordClass;
    private static boolean mEnabled;
    private static boolean mDebug;

    private static XSharedPreferences getPreferences() {
        if (mPreferences == null) {
            mPreferences = new XSharedPreferences("space.hamsters.fspammer");
            mPreferences.makeWorldReadable();
            mPreferences.reload();
        }
        return mPreferences;
    }

    private static void findClasses(ClassLoader loader) {
        if (MsgClass == null)
            MsgClass = XposedHelpers.findClass("msf.msgcomm.msg_comm$Msg", loader);
        if (PBDecodeContextClass == null)
            PBDecodeContextClass = XposedHelpers.findClass("com.tencent.mobileqq.service.message.PBDecodeContext", loader);
        if (MessageInfoClass == null)
            MessageInfoClass = XposedHelpers.findClass("com.tencent.mobileqq.troop.data.MessageInfo", loader);
        if (MessageRecordClass == null)
            MessageRecordClass = XposedHelpers.findClass("com.tencent.mobileqq.data.MessageRecord", loader);
    }

    public static void hookBaseMessageProcessorForTroop(final ClassLoader loader) {
        if (!getPreferences().getBoolean("blocker_enabled", false)) {
            XposedBridge.log("FSpammer is disabled");
            return;
        }

        findClasses(loader);
        mEnabled = getPreferences().getBoolean("blocker_enabled", false);
        mDebug = getPreferences().getBoolean("debug_enabled", false);

        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.app.message.BaseMessageProcessorForTroopAndDisc", loader,
                "a", MsgClass, ArrayList.class, PBDecodeContextClass, boolean.class, MessageInfoClass,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        if (!mEnabled)
                            return;
                        findClasses(loader);
                        ArrayList list = (ArrayList) param.args[1];
                        if (list.size() > 0) {
                            Object msgRecord = list.get(0);
                            logd(msgRecord.toString());
                            String msg = MessageRecordReader.getMessageRecordContent(msgRecord);
                            if (msg != null && msg.length() > 0) {
                                logd("Complete message: " + msg);
                                if (msg.matches(getPreferences().getString("block_regex", ""))) {
                                    logd("Message above is filtered");
                                    list.clear();
                                }
                            }
                        }
                    }
                });
        XposedBridge.log("FSpammer hooked on QQ");
    }

    private static void logd(String log) {
        if (mDebug) XposedBridge.log(log);
    }

    private static class MessageRecordReader {
        public static String getMessageRecordContent(Object messageRecord) {
            try {
                Field msgField = MessageRecordClass.getField("msg");
                msgField.setAccessible(true);
                return (String) msgField.get(messageRecord);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                /* IllegalAccessException should not happen */
                XposedBridge.log(e);
            }
            return "";
        }
    }
}
