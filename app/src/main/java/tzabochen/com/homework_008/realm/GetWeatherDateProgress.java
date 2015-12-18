package tzabochen.com.homework_008.realm;

import android.app.ProgressDialog;
import android.content.Context;

import tzabochen.com.homework_008.R;

public class GetWeatherDateProgress extends GetWeatherDate
{
    // VALUE'S
    private ProgressDialog progressDialog;
    private Context context;

    public GetWeatherDateProgress()
    {
    }

    public GetWeatherDateProgress(Context context)
    {
        this.context = context;
        setProgressDialog();
    }

    @Override
    protected void onPreExecute()
    {
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        progressDialog.dismiss();
        super.onPostExecute(aVoid);
    }

    private void setProgressDialog()
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(context.getString(R.string.progress_dialog));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
}
