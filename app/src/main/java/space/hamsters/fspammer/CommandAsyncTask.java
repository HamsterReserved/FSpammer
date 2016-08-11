package space.hamsters.fspammer;

import android.os.AsyncTask;

/**
 * Created by hamster on 16/8/11.
 */
public class CommandAsyncTask extends AsyncTask<String, Void, Integer> {
    private OnFinishCallback mCallback;

    CommandAsyncTask(OnFinishCallback callback) {
        mCallback = callback;
    }

    @Override
    protected Integer doInBackground(String... params) {
        return RootHelper.executeAsRoot(params[0]);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (mCallback == null) return;

        if (integer.equals(0)) {
            mCallback.onSuccess();
            return;
        }
        mCallback.onFailure();
    }

    public interface OnFinishCallback {
        void onSuccess();

        void onFailure();
    }
}
