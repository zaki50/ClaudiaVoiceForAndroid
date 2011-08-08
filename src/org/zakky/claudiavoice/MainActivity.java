/*
 * Copyright 2011 YAMAZAKI Makoto<makoto1975@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zakky.claudiavoice;

import java.io.IOException;
import java.io.InputStream;

import android.app.ListActivity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * クラウディアのボイスリストを表示し、タップされたアイテムに関連付けられている音声を再生する
 * アクティビティです。
 */
public class MainActivity extends ListActivity {

    /**
     * ポートレイト(縦長)モードの際に使用可能な背景画像アセット名の配列です。
     */
    private static final String[] BG_PORT = {
            "claudia_port_sd1.jpg", "claudia_port_sd2.jpg", "claudia_port_sd3.jpg",
            "claudia_port_sd4.jpg", "claudia_port_ushiro.jpg", "claudia_port.jpg",
            "claudia_port_naname.jpg",
    };

    /**
     * ランドスケープ(横長)モードの際に使用可能な背景画像アセット名の配列です。
     */
    private static final String[] BG_LAND = {
            "claudia_land_salute.jpg", "claudia_land_desk.jpg",
    };

    /**
     * {@link #BG_PORT} のうち、どれを背景として使用するかを示すインデックスです。
     */
    private static int sBgIndexForPort = -1;

    /**
     * {@link #BG_LAND} のうち、どれを背景として使用するかを示すインデックスです。
     */
    private static int sBgIndexForLand = -1;

    /**
     * 音声リソースリストです。
     */
    private static final int[] VOICES = {
            R.raw.voice_01sahajimeru, R.raw.voice_03syuuryou, R.raw.voice_04oujougiwa,
            R.raw.voice_05okazure, R.raw.voice_06windowsazureha, R.raw.voice_07azurenisite,
            R.raw.voice_08windowsazure, R.raw.voice_09cloudwoyoro, R.raw.voice_10microsoft,
            R.raw.voice_11mangadewakaru, R.raw.voice_12sorenara, R.raw.voice_13blue,
            R.raw.voice_14azurenojituryoku, R.raw.voice_16finiiish, R.raw.voice_17goo,
            R.raw.voice_18par, R.raw.voice_19choki, R.raw.voice_20aikogoo, R.raw.voice_21aikopar,
            R.raw.voice_22aikochoki, R.raw.voice_27goodmorningazuresky, R.raw.voice_28denwa,
            R.raw.voice_29mail, R.raw.voice_30start, R.raw.voice_31deploykaishi,
            R.raw.voice_32deploykanryo, R.raw.voice_33build, R.raw.voice_34sippai,
            R.raw.voice_35daiseikou, R.raw.voice_36ei, R.raw.voice_37ah, R.raw.voice_38otsukare,
            R.raw.voice_39kokogapoint, R.raw.voice_40chui,
    };

    /**
     * 背景イメージを保持する {@link View} です。
     */
    private ImageView mBackground;

    /**
     * 現在の背景イメージビットマップです。
     */
    private Bitmap mBackgroundBitmap;

    /**
     * ボイス再生用のボイス再生用のプレーヤーです。
     */
    private MediaPlayer mPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBackground = (ImageView) findViewById(R.id.background);

        final String[] assetLabels = getResources().getStringArray(R.array.voice_labels);
        assert assetLabels.length == VOICES.length;

        final ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.voice_row, assetLabels);
        setListAdapter(adapter);

        final ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.release();
                }
                mPlayer = MediaPlayer.create(MainActivity.this, VOICES[position]);
                mPlayer.start();
                mPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
            }
        });
        // このアクティビティ表示中はボリュームキーでメディアの音量が変わるようにする
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBackground.setImageResource(R.drawable.dummy_bg);
        if (mBackgroundBitmap != null) {
            mBackgroundBitmap.recycle();
            mBackgroundBitmap = null;
        }

        final String assetName = getNextBackground();
        try {
            final InputStream bgStream = getAssets().open(assetName);
            try {
                mBackgroundBitmap = BitmapFactory.decodeStream(bgStream);
            } finally {
                try {
                    bgStream.close();
                } catch (IOException e) {
                    // ignore
                    assert true;
                }
            }
            mBackground.setImageBitmap(mBackgroundBitmap);
        } catch (IOException e) {
            throw new RuntimeException("failed to load background image: " + assetName, e);
        }
    }

    /**
     * 次に使用する背景画像のアセット名を返します。
     * 
     * <p>
     * 背景画像は、端末がポートレイトかランドスケープかを判定して、適切な値を返します。
     * </p>
     * @return
     * 背景画像アセット名。
     */
    private String getNextBackground() {
        if (isLandscapeMode()) {
            sBgIndexForLand++;
            if (BG_LAND.length <= sBgIndexForLand) {
                sBgIndexForLand = 0;
            }
            return BG_LAND[sBgIndexForLand];
        }

        // for portrait
        sBgIndexForPort++;
        if (BG_PORT.length <= sBgIndexForPort) {
            sBgIndexForPort = 0;
        }
        return BG_PORT[sBgIndexForPort];
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPlayer != null) {
            mPlayer.stop();
        }
        mPlayer = null;

        mBackground.setImageBitmap(null);
    }

    /**
     * 現在の画面がランドスケープ(横長)モードかどうかを返します。
     * @return
     * ランドスケープモードであれば {@code true}、ポートレイトモードであれば {@code false}。
     */
    private boolean isLandscapeMode() {
        final int orientation = getResources().getConfiguration().orientation;
        final boolean isLand = (orientation == Configuration.ORIENTATION_LANDSCAPE);
        return isLand;
    }
}
