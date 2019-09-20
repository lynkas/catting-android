package net.catting.android;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.catting.android.data.S;

import java.io.File;

public class UpdateActivity extends AppCompatActivity {

    private TextView title;
    private TextView version;
    private TextView log;
    private Button button;
    private String  url;

    private static final int RQ_GRANT_PERMISSION=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkUpdate();

        title=findViewById(R.id.update_title);
        version=findViewById(R.id.update_version);
        log=findViewById(R.id.update_log);
        button=findViewById(R.id.update_button);

    }

    boolean checkUpdate(){

        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON(S.UPDATE_JSON)
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {

                        if (isUpdateAvailable){
                            url=update.getUrlToDownload().toString();
                            title.setText(R.string.update_available);
                            version.setText(update.getLatestVersion());
                            version.setVisibility(View.VISIBLE);
                            log.setText(update.getReleaseNotes());
                            log.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            button.setText(R.string.update_now);


                            button.setOnClickListener((view)->{
                                CheckStoragePermission();
                            });
                        }else {
                            title.setText(R.string.no_update_available);
                        }
                        Log.d("Latest Version", update.getLatestVersion());
                        Log.d("Latest Version Code", update.getLatestVersionCode().toString());
                        Log.d("Release notes", update.getReleaseNotes());
                        Log.d("URL", update.getUrlToDownload().toString());
                        Log.d("Is update available?", Boolean.toString(isUpdateAvailable));
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {

                        Log.d("AppUpdater Error", "Something went wrong");
                        Log.d("AppUpdater Error", error.toString());
                    }
                });

        appUpdaterUtils.start();
        return true;
    }

    void download(String url){
        //get destination to update file and set Uri
        //TODO: First I wanted to store my update .apk file on internal storage for my app but apparently android does not allow you to open and install
        //aplication with existing package from there. So for me, alternative solution is Download directory in external storage. If there is better
        //solution, please inform us in comment
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "catting-latest.apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists())
            //file.delete() - test this, I think sometimes it doesnt work
            file.delete();

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(this.getString(R.string.downloading_notification));
        request.setTitle(this.getString(R.string.app_name));

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                Uri file = manager.getUriForDownloadedFile(downloadId);
//                Uri file = FileProvider.getUriForFile(context,
//                        context.getApplicationContext().getPackageName() + ".fileprovider"
//                        ,new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+ "/" + fileName) );
                Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.setDataAndType(file,
                        manager.getMimeTypeForDownloadedFile(downloadId));
                Log.d("a", "onReceive: "+manager.getMimeTypeForDownloadedFile(downloadId));
                context.startActivity(install);

                unregisterReceiver(this);

            }
        };
        //register receiver for when .apk download is compete
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void CheckStoragePermission() {
        //Android 6.0檢查是否開啟儲存(WRITE_EXTERNAL_STORAGE)的權限，若否，出現詢問視窗
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement
//
//        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                RQ_GRANT_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case RQ_GRANT_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,R.string.downloading_please_wait_for_a_while,Toast.LENGTH_LONG).show();
                    download(url);
                } else {
                    Toast.makeText(this,R.string.need_permission_on_storage,Toast.LENGTH_LONG).show();
                }
                return;
        }
    }
}
