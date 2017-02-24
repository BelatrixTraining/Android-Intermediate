package com.bx.android.lesson2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SharedPreferences.Editor editor = getSharedPreferences("sdf", Context.MODE_PRIVATE).edit();
//        editor.commit();

//        writeInFile();
//        String content = readFromPrivateFile();
//
//        String content2 = readFromRawDirectory();
//
//        TextView textView = (TextView) findViewById(R.id.textView);
//        textView.setText(content2);

        File file = getAlbumStorageDir("Pablo2");
        String[] files = file.list();
        for (String fileName : files) {
            Log.e("Pablo", fileName);
        }
        String asd = "asd";
    }

    private String readFromRawDirectory() {
        StringBuffer fileContent = new StringBuffer("");
        InputStream is = getResources().openRawResource(R.raw.belatrix);

        byte[] buffer = new byte[1024];
        int n = 0;
        try {
            while ((n = is.read(buffer)) != -1) {
                fileContent.append(new String(buffer, 0, n));
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent.toString();
    }

    private String readFromPrivateFile() {

        StringBuffer fileContent = new StringBuffer("");
        try {
            String FILENAME = "hello_file";

            FileInputStream fis = openFileInput(FILENAME);

            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = fis.read(buffer)) != -1) {
                fileContent.append(new String(buffer, 0, n));
            }
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }

    private void writeInFile() {
        try {
            String FILENAME = "hello_file";
            String string = "hello Belatrix world!";

            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("Pablo", "Directory not created");
        }
        return file;
    }


}
