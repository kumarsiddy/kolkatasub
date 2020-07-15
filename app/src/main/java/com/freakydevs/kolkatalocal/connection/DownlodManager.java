package com.freakydevs.kolkatalocal.connection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by PURUSHOTAM on 11/23/2017.
 */

public class DownlodManager {
    private static String DATABASE_PATH = "";
    private static String DATABASE_NAME = "database";
    Context context;
    KProgressHUD hud;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public DownlodManager(Context context) {
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).toString();
    }

    public void startDownload() {


        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("DataBase is Downloading...")
                .setMaxProgress(100);
        hud.show();

        StorageReference dbRef = storage.getReference().child("kolkata_sub/database.zip");
        dbRef.getBytes(1024 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {


                int count;
                try {
                    ZipInputStream zis = null;
                    FileOutputStream outputStream = null;
                    ZipEntry zipEntry;
                    long totalSize;
                    outputStream = new FileOutputStream(new File(DATABASE_PATH));
                    zis = new ZipInputStream(new ByteArrayInputStream(bytes));

                    byte[] Buffer = new byte[1024];


                    long total = 0;
                    while ((zipEntry = zis.getNextEntry()) != null) {
                        while ((count = zis.read(Buffer)) != -1) {
                            total += count;
                            totalSize = zipEntry.getSize();
                            outputStream.write(Buffer, 0, count);
                           /* if (isCancelled()) {
                                break;
                            }*/
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
                }

                if (hud.isShowing()) {
                    hud.dismiss();
                }

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
