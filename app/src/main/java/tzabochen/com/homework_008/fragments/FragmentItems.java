package tzabochen.com.homework_008.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;
import tzabochen.com.homework_008.realm.GetWeatherDate;
import tzabochen.com.homework_008.realm.MyRealmBaseAdapter;
import tzabochen.com.homework_008.realm.RealmWeather;
import tzabochen.com.homework_008.tools.ItemSelectedListener;

public class FragmentItems extends ListFragment
{
    // VALUE'S
    private Realm realm;
    private ItemSelectedListener itemSelectedListener;
    public static MyRealmBaseAdapter adapter;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        itemSelectedListener = (ItemSelectedListener) context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // ASYNC TASK -> LOAD & PARSE & ADD & CLEAR
        new GetWeatherDate().execute(getContext());

        // REALM INSTANCE
        realm = Realm.getInstance(getContext());
        RealmResults<RealmWeather> realmResults = realm.where(RealmWeather.class).findAll();

        // ADAPTER
        adapter = new MyRealmBaseAdapter(getActivity(), realmResults, true);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // SEND SELECTED POSITION
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        itemSelectedListener.itemSelected(position);
    }
}