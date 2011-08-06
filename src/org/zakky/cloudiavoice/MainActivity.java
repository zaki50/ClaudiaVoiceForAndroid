
package org.zakky.cloudiavoice;

import android.app.ListActivity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final int[] assetNames = {
                R.raw.voice_01sahajimeru,
                R.raw.voice_03syuuryou,
                R.raw.voice_04oujougiwa,
                R.raw.voice_05okazure,
                R.raw.voice_06windowsazureha,
                R.raw.voice_07azurenisite,
                R.raw.voice_08windowsazure,
                R.raw.voice_09cloudwoyoro,
                R.raw.voice_10microsoft,
                R.raw.voice_11mangadewakaru,
                R.raw.voice_12sorenara,
                R.raw.voice_13blue,
                R.raw.voice_14azurenojituryoku,
                R.raw.voice_16finiiish,
                R.raw.voice_17goo,
                R.raw.voice_18par,
                R.raw.voice_19choki,
                R.raw.voice_20aikogoo,
                R.raw.voice_21aikopar,
                R.raw.voice_22aikochoki,
                R.raw.voice_27goodmorningazuresky,
                R.raw.voice_28denwa,
                R.raw.voice_29mail,
                R.raw.voice_30start,
                R.raw.voice_31deploykaishi,
                R.raw.voice_32deploykanryo,
                R.raw.voice_33build,
                R.raw.voice_34sippai,
                R.raw.voice_35daiseikou,
                R.raw.voice_36ei,
                R.raw.voice_37ah,
                R.raw.voice_38otsukare,
                R.raw.voice_39kokogapoint,
                R.raw.voice_40chui,
        };

        final String[] assetLabels = getResources().getStringArray(R.array.voice_labels);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, assetLabels);

        setListAdapter(adapter);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, assetNames[position]);
                mp.start();
                //mp.setOnCompletionListener(RELEASE_PLAYER_LISTENER);
                mp = null;
            }
        });
    }

}
