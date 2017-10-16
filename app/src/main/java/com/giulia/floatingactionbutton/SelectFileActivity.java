package com.giulia.floatingactionbutton;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

/**
 * @author paulburke (ipaulpro)
 */
public class SelectFileActivity extends Activity {


    private static final String TAG = "SelectFileActivity";
    private static final int ID_RICHIESTA_PERMISSION = 1;
    File FILE_PATH_SDCARD = Environment.getExternalStorageDirectory();
    File fileDir;
    File extractFileName;
    String fileName,choosedFile;

    private static final int REQUEST_CODE = 6384; // onActivityResult request
    // code
    File myFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create a simple button to start the file chooser process
        Button button2,button;
        button=(Button) findViewById(R.id.button);
        button2=(Button) findViewById(R.id.button2);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRadice();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the file chooser dialog

                showChooser();
            }
        });


    }

    private void loadRadice(){
        final AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(SelectFileActivity.this);
        final View mView1 = getLayoutInflater().inflate(R.layout.dialog_newfile, null);
        final EditText file = (EditText) mView1.findViewById(R.id.nomefile);


        mBuilder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!file.getText().toString().isEmpty()) {
                    Toast.makeText(SelectFileActivity.this, "File aggiunto", Toast.LENGTH_SHORT).show();
                    String r1 = file.getText().toString();
                    Intent nextActivity = new Intent(SelectFileActivity.this, Radice.class);
                    nextActivity.putExtra("choosed file", r1);
                    startActivity(nextActivity);
                    finish();

                }

            }
        });


        mBuilder1.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });


        mBuilder1.setView(mView1);
        AlertDialog dialog1 = mBuilder1.create();
        dialog1.show();


    }
    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Toast.makeText(SelectFileActivity.this,
                                    "File Selected: " + path, Toast.LENGTH_LONG).show();
                                    //Quando seleziono il file lo passo all' activity che farà il parsing di quel file
                                    Intent nextActivity=new Intent(SelectFileActivity.this,Radice.class);
                                    if(!FileUtils.getExtension(path).equals(".json"))
                                        Toast.makeText(getApplicationContext(),"File non valido",Toast.LENGTH_LONG).show();
                                    else{

                                        //Estraggo il nome del file senza l'estensione
                                        extractFileName=new File(path);
                                        choosedFile=extractFileName.getName();
                                        String extension=FileUtils.getExtension(choosedFile);
                                        fileName=choosedFile.substring(0,choosedFile.length()-extension.length());

                                       nextActivity.putExtra("choosed file",fileName);
                                    startActivity(nextActivity);


                        }}
                        catch (Exception e) {
                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
