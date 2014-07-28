package co.touchlab.android.threading.tasks;

import android.content.Context;
import co.touchlab.android.threading.utils.UiThreadContext;
import de.greenrobot.event.EventBus;

/**
 * Simple AsyncTask replacement.
 *
 * First the name.  AsyncTask/BsyncTask.  Async/Bsync.  A/B.
 *
 * The task will probably hold onto a reference to the context, which is probably an
 * Activity/Fragment.  If your tasks are long running, keep that in mind.
 *
 * Created by kgalligan on 7/5/14.
 */
public abstract class BsyncTask<D> implements TaskQueue.Task
{
    protected int contextId;
    private boolean cancel;

    protected abstract void doInBackground(Context context)throws Exception;

    protected abstract void onPostExecute(D host);

    protected void cancelPost()
    {
        UiThreadContext.assertBackgroundThread();
        cancel = true;
    }

    @Override
    public final void run(Context context) throws Exception
    {
        UiThreadContext.assertBackgroundThread();
        doInBackground(context);
        if(!cancel)
            EventBus.getDefault().post(this);
    }
}
