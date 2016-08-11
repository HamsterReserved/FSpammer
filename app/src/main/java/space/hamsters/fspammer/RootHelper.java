package space.hamsters.fspammer;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by hamster on 16/8/11.
 * <p/>
 * Helper class to run commands as root.
 * You may want to wrap this in AsyncTask.
 */
public class RootHelper {
    public static int executeAsRoot(String cmdline) {
        Process suProcess;
        try {
            suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(suProcess.getOutputStream());

            out.writeBytes(cmdline + "\n");
            out.flush();

            out.writeBytes("exit\n");
            out.flush();

            return suProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 255;
        }
    }
}
