
package org.zakky.cloudiavoice;

import java.util.Random;

import android.app.ListActivity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends ListActivity {

    private ImageView mBackground;

    private MediaPlayer mPlayer;

    private static final Random sRandom = new Random();

    private static final int[] BG_PORT = {
        R.drawable.cloudia_port,
        R.drawable.cloudia_port_naname,
        R.drawable.cloudia_port_sd1,
        R.drawable.cloudia_port_sd2,
        R.drawable.cloudia_port_sd3,
        R.drawable.cloudia_port_sd4,
        R.drawable.cloudia_port_ushiro,
    };

    private static final int[] BG_LAND = {
        R.drawable.cloudia_land_desk,
        R.drawable.cloudia_land_salute,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBackground = (ImageView) findViewById(R.id.background);

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

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.voice_row,
                assetLabels);

        setListAdapter(adapter);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                if (mPlayer != null) {
                    mPlayer.stop();
                }
                mPlayer = MediaPlayer.create(MainActivity.this, assetNames[position]);
                mPlayer.start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final int id =  getBgResourceId();
        mBackground.setImageResource(id);
    }

    private int getBgResourceId() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            final int value = sRandom.nextInt(BG_LAND.length);
            Log.e("Rand", "value = " + value);
            return BG_LAND[value];
        }

        // for portrait
        final int value = sRandom.nextInt(BG_PORT.length);
        Log.e("Rand", "value = " + value);
        return BG_PORT[value];
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPlayer != null) {
            mPlayer.stop();
        }
        mPlayer = null;
    }

}
