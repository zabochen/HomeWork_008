package tzabochen.com.homework_008.realm;

import android.content.Context;
import android.widget.Toast;

import tzabochen.com.homework_008.R;

public class GetWeatherDateNotify extends GetWeatherDate
{
    // VALUE'S
    private Context context;

    public GetWeatherDateNotify()
    {
    }

    public GetWeatherDateNotify(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);

        // SHOW TOAST
        StringBuilder updateCompleted = new StringBuilder()
                .append(context.getString(R.string.app_name))
                .append(": ")
                .append(context.getString(R.string.toast_update_completed));

        Toast.makeText(context, updateCompleted.toString(), Toast.LENGTH_SHORT).show();
    }
}
