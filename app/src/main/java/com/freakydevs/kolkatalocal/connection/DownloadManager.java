package com.freakydevs.kolkatalocal.connection;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.freakydevs.kolkatalocal.customview.MyToast;
import com.freakydevs.kolkatalocal.utils.Logger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.zip.ZipInputStream;

/**
 * Created by PURUSHOTAM on 11/23/2017.
 */

public class DownloadManager {

    private static String DATABASE_PATH = "";
    private static String DATABASE_NAME = "database";
    public FirebaseStorage storage = FirebaseStorage.getInstance();
    Context context;
    KProgressHUD hud;

    public DownloadManager(Context context) {
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).toString();
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setMaxProgress(100)
                .setCancellable(false)
                .setLabel("DataBase is Downloading...");
        downloadDatabase();
        hud.show();
    }

    private void downloadDatabase() {
        StorageReference dbRef = storage.getReference().child("kolkata_sub/database.zip");

        File file = null;
        try {
            file = File.createTempFile("database", "zip");
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileDownloadTask task = dbRef.getFile(file);

        final File finalFile = file;
        task.addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                double fprogress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                long bytes = taskSnapshot.getBytesTransferred();

                Logger.d("Data: " + bytes);

                Logger.d("Progress = " + fprogress + "%");

                hud.setProgress((int) fprogress);

            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                MyToast.showToast(context.getApplicationContext(), "Download Successfull!");

                unZip(finalFile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ", ";local tem file not created  created " + exception.toString());
            }
        });
    }

    private void unZip(File finalFile) {

        int count;

        try {
            ZipInputStream zis = null;
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream(new File(DATABASE_PATH));
            zis = new ZipInputStream(new FileInputStream(finalFile));

            byte[] Buffer = new byte[1024];

            while ((zis.getNextEntry()) != null) {
                while ((count = zis.read(Buffer)) != -1) {
                    outputStream.write(Buffer, 0, count);
                }
            }
            // flushing output
            outputStream.flush();

            // closing streams
            outputStream.close();
            zis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalFile.delete();
        }
    }

}
