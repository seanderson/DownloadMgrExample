package com.example.downloadmgrexample;


import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends Activity {
    private long enqueue;
    private DownloadManager dm;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // receiver that will respond to completed downloads.
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Query query = new Query(); // for querying download about all downloads
                    query.setFilterById(enqueue); // get responses to last request made when button pushed
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) { // Show one retrieval if it is there.
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        // See if download is successful by checking cursor
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {
 
                            ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            view.setImageURI(Uri.parse(uriString));
                        }
                    }
                }
            }
        };
        // REgister to receive the completed notifications
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
 
    /**
     * Create and execute request for data.
     * @param view
     */
    public void onClick(View view) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Request request = new Request(
                Uri.parse("http://critterbabies.com/wp-content/uploads/2013/11/kittens-300x199.jpg"));
        // ID of request
        enqueue = dm.enqueue(request);
     }
 
    /**
     * Start intent to view all downloads.
     * @param view
     */
    public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }
}