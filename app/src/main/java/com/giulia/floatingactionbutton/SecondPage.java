package com.giulia.floatingactionbutton;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SecondPage extends AppCompatActivity {
    ArrayList<Integer> arrayImages, arrayImages1;
    //ArrayList con il nome delle cartelle e/o file:
    ArrayList<String> arrayItemNames, arrayItemNames1;
    //Gli array sopra dichiarati mi servono per riempire la listview
    //Struttura dati b-albero per tenere in memoria le cartelle ecc
    ArrayList<SelectedItem> arraySelectedItems;
    //BTree<Myobject> allMyfiles;
    ArrayList<Myobject> arrayMyObjects;
    ArrayList<String> currentPath;
    ArrayList<Fragment> fragment;
    TextView textView1, textView11, textView0;
    ListView listView1;
    File FILE_PATH_SDCARD = Environment.getExternalStorageDirectory();
    File FILE_DIRECTORIES = new File(FILE_PATH_SDCARD, "Documents/l.json");
    File FILE_FILES = new File(FILE_PATH_SDCARD, "Documents/l1.json");
    int l = 0;
    JSONArray arrayDirectory, arrayFile;
    SelectedItem s = new SelectedItem();

    String currentDir, currentDir1, reading, reading2;
    String nomeCartella;
    String displayPath = "";
    int count = 0;
    int j = 0;
    Bundle datipassati;
    Myadapter myAdapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        final List selection=new ArrayList<>();
        listView1 = (ListView) findViewById(R.id.listView1);
        final TextView textView0 = (TextView) findViewById(R.id.textView0);
        nomeCartella = "Tutti i file";
        arraySelectedItems = new ArrayList<>();
        arrayImages=new ArrayList<>();
        arrayItemNames = new ArrayList<>();
        myAdapter=new Myadapter(SecondPage.this, arrayImages, arrayItemNames);
        currentPath = new ArrayList<>();
        currentPath.add(nomeCartella);
        currentDir = "/" + currentPath.get(0);
        textView0.setText(currentDir);

        // toolbar.setTitle(currentDir);

        // arrayobj = new ArrayList<>();
        listView1.setAdapter(myAdapter);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView11 = (TextView) findViewById(R.id.textView11);

        reading = leggiFile(FILE_DIRECTORIES);
        reading2 = leggiFile(FILE_FILES);
        parseJson(arrayImages, arrayItemNames, nomeCartella);
        //registerForContextMenu(listView1);


        textView1.setText(reading);
        textView11.setText(reading2);


        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                String displayPath = "";
                nomeCartella = (String) listView1.getItemAtPosition(pos);
                Integer id1 = myAdapter.getImageId(pos);
                if (id1.equals(R.drawable.ic_folder_grey)) {


                    if (nomeCartella.equals("..")) {
                        currentPath.remove(currentPath.size() - 1);
                        if (currentPath.size() == 0) {

                            Intent intent = new Intent(SecondPage.this, Radice.class);
                            startActivity(intent);

                        } else
                            nomeCartella = currentPath.get(currentPath.size() - 1);

                    } else {
                        if (currentPath.size() == 0)
                            currentPath.add(currentDir);

                        currentPath.add(nomeCartella);


                    }
                    // monto il path corrente
                    for (String elem : currentPath) {
                        displayPath += "/" + elem;
                    }

                    textView0.setText(displayPath);
                    myAdapter.setData(arrayImages=new ArrayList<Integer>(),arrayItemNames=new ArrayList<String>());
                    parseJson(arrayImages, arrayItemNames, nomeCartella);


                }


            }
        });

        listView1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        listView1.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
             if(checked)
             {

                 s.setNome((String) listView1.getItemAtPosition(position));
                 s.setPos(position);
                 arraySelectedItems.add(s);
                 count++;
                 mode.setTitle(count + "elementi selezionati");
             }
             else
             {   arraySelectedItems.remove(s);
                 //selection.remove(listView1.getItemAtPosition(position));
                 count--;
                 mode.setTitle(count + "elementi selezionati");
             }
                }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.contextmenu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

                switch (arraySelectedItems.size()) {
                    case 1:

                        menu.getItem(0).setEnabled(true);
                        menu.getItem(1).setEnabled(true);
                        return true;
                    default:
                        menu.getItem(0).setEnabled(true);
                        menu.getItem(1).setEnabled(false);
                        return true;
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:

                        elimina(arraySelectedItems, arrayImages, arrayItemNames, nomeCartella);
                        Toast.makeText(getBaseContext(), count + "elementi eliminati", Toast.LENGTH_SHORT).show();
                        //textView1.setText(arrayRoot.toString());
                        myAdapter.notifyDataSetChanged();
                        String r = leggiFile(FILE_DIRECTORIES);
                        textView1.setText(r);
                        count = 0;
                        mode.finish();
                        return true;

                    case R.id.rename:
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SecondPage.this);
                        final View mView = getLayoutInflater().inflate(R.layout.dialog_rename, null);
                        final EditText rinomina = (EditText) mView.findViewById(R.id.rinomina_cartella);


                        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!rinomina.getText().toString().isEmpty()) {
                                    Toast.makeText(SecondPage.this, "Elemento rinominato", Toast.LENGTH_SHORT).show();
                                    String s = rinomina.getText().toString();

                                    rename(arraySelectedItems,arrayImages, arrayItemNames , s, nomeCartella);
                                }
                            }
                        });

                        mBuilder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        });
                        mBuilder.setView(mView);
                        AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        mode.finish();
                        return true;


                    default:

                        return false;
                }


            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                count=0;
                arraySelectedItems.clear();

            }
        });

    }




    //end of OnCreate
    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.animation_textview);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.textView0);
        tv.clearAnimation();
        tv.startAnimation(a);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addfolder:

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SecondPage.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_newfolder, null);
                final EditText cartella = (EditText) mView.findViewById(R.id.nome_cartella);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!cartella.getText().toString().isEmpty()) {
                            Toast.makeText(SecondPage.this, "Cartella aggiunta", Toast.LENGTH_SHORT).show();
                            String r = cartella.getText().toString();
                            createDirectory(arrayImages,arrayItemNames, r, nomeCartella);

                            ;
                            //textview.setText(str);

                        }
                    }
                });
                mBuilder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
                return true;

            case R.id.addfile:
                final AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(SecondPage.this);
                final View mView1 = getLayoutInflater().inflate(R.layout.dialog_newfile, null);
                final EditText file = (EditText) mView1.findViewById(R.id.nomefile);

                mBuilder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!file.getText().toString().isEmpty()) {
                            Toast.makeText(SecondPage.this, "File aggiunto", Toast.LENGTH_SHORT).show();
                            String r1 = file.getText().toString();
                            createFile(arrayImages,arrayItemNames, r1,nomeCartella);

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


                return true;
            case R.id.home:
                Intent primaPagina = new Intent(SecondPage.this,LoadContents.class);
                startActivity(primaPagina);

                return true;

            default:
                return false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(nomeCartella.equals("Tutti i file")){
            Intent intent = new Intent(SecondPage.this, Radice.class);
            startActivity(intent);
            return true;}

        }

        return false;
    }

    private void createDirectory(ArrayList<Integer> immagini, ArrayList<String> nomi, String r1, String nomeCart) {
        JSONObject obj_nuovo1, obj_nuovo2, obj_nuovo3;
        JSONArray array_nuovo1, array_nuovo2, parse;
        obj_nuovo1 = new JSONObject();
        array_nuovo1 = new JSONArray();
        obj_nuovo2 = new JSONObject();
        obj_nuovo3 = new JSONObject();
        array_nuovo2 = new JSONArray();

        String risultato = leggiFile(FILE_DIRECTORIES);
        int flag = 0;
        //Creare un nuovo oggetto e aggiungerlo all'albero
        //Myobject o = new Myobject(r, "dir", false);
        //arrayMyObjects.add(o);
        //AGGIUNGO ALLA LISTVIEW GLI ELEMENTI
       // myAdapter.setData(immagini,nomi);
        nomi.add(r1);
        immagini.add(R.drawable.ic_folder_grey);
        //CREO L'OGGETTO JSON CORRISPONDENTE ALLA SCRITTURA
        /*
        * Devo creare un oggetto json da inserire nell'array json principale
        * e uno da inserire nell'array corrispondente alla cartella dove voglio creare il file
        * */
        try {
            parse = new JSONArray(risultato);
            obj_nuovo1.put(r1, array_nuovo1);
            parse.put(obj_nuovo1);
            obj_nuovo3.put(r1, "dir");

            for (int i = 0; i < parse.length(); i++) {
                obj_nuovo2 = parse.getJSONObject(i);
                Iterator iterator = obj_nuovo2.keys();
                while (iterator.hasNext() && flag == 0) {
                    String key = (String) iterator.next();
                    if (key.equals(nomeCart)) {
                        flag = 1;
                        array_nuovo2 = obj_nuovo2.getJSONArray(key);
                        array_nuovo2.put(obj_nuovo3);

                    }
            }}
            risultato = parse.toString();
            textView1.setText(risultato);
            scriviFile(risultato, FILE_DIRECTORIES);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createFile(ArrayList<Integer> immagini, ArrayList<String> nomi, String r,String nomeCart) {
        JSONObject obj_nuovo1, obj_nuovo2, obj_nuovo3;
        JSONArray parse, array_nuovo1, array_nuovo2;
        obj_nuovo1 = new JSONObject();
        array_nuovo1 = new JSONArray();
        obj_nuovo2 = new JSONObject();
        obj_nuovo3 = new JSONObject();
        array_nuovo2 = new JSONArray();
        String risultato = leggiFile(FILE_FILES);
        String key;
        int flag = 0;

        //Creare un nuovo oggetto e aggiungerlo all'albero
        //Myobject o = new Myobject(r, "file", false);
        //arrayMyObjects.add(o);

        //AGGIUNGO ALLA LISTVIEW GLI ELEMENTI
        nomi.add(r);
        immagini.add(R.drawable.ic_file_blue);
        //CREO L'OGGETTO JSON CORRISPONDENTE PER LA SCRITTURA

        try {
            parse = new JSONArray(risultato);
            for (int i = 0; i < parse.length() && flag == 0; i++) {
                obj_nuovo2 = parse.getJSONObject(i);
                Iterator iterator = obj_nuovo2.keys();
                while (iterator.hasNext() && flag == 0) {
                    key = (String) iterator.next();
                    if (key.equals(nomeCart)) {
                        flag = 1;
                        array_nuovo2 = obj_nuovo2.getJSONArray(key);
                        obj_nuovo3.put(r, "file");
                        array_nuovo2.put(obj_nuovo3);

                    }
                }


                }
            if (flag == 0) {
                obj_nuovo3.put(r,"file");
                array_nuovo1.put(obj_nuovo3);
                obj_nuovo1.put(nomeCart, array_nuovo1);
                parse.put(obj_nuovo1);

            }

            risultato = parse.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        textView11.setText(risultato);
        scriviFile(risultato, FILE_FILES);

    }

    private void rename(ArrayList<SelectedItem> selezionati, ArrayList<Integer> immagini, ArrayList<String> nomi, String s1, String nomeCart) { //devo passare l'array contenente i miei oggetti
        String temp, temp1;
        String elem = selezionati.get(0).getNome();

        int k = selezionati.get(0).getPos();
        int flag = 0;
        int flag1 = 0;
        int flag2=0;
        int flag3=0;
        int i = 0;

        JSONArray arrayUno;
        JSONArray arrayDue=new JSONArray();
        JSONArray arrayTre;

        JSONObject objUno= new JSONObject();
        JSONObject objDue=new JSONObject();
        JSONObject objTre=new JSONObject();


        //RINOMINO SULLA LISTVIEW L'ELEMENTO
       // myAdapter.setData(immagini,nomi);
        nomi.set(k, s1);
        //AZZERO LA LISTA DEI SELEZIONATI
        selezionati.clear();
        try {
            if (immagini.get(k).equals(R.drawable.ic_folder_grey)) {
                temp = leggiFile(FILE_DIRECTORIES);
                arrayUno = new JSONArray(temp);
                    for (int index = 0; index < arrayUno.length() && flag == 0; index++) {
                        objDue = arrayUno.getJSONObject(index);
                        Iterator iterator = objDue.keys();
                        while (iterator.hasNext() && flag == 0) {
                            String key = (String) iterator.next();
                            //se la chiave è uguale al nome della cartella dentro cui voglio rinominare
                            //accedo
                            if (key.equals(nomeCart)&&flag2==0) {
                                flag2=1;
                                arrayDue = objDue.getJSONArray(key);
                                for (int j = 0; j < arrayDue.length() && flag1 == 0; j++) {
                                    objTre = arrayDue.getJSONObject(j);
                                    Iterator iterator1 = objTre.keys();
                                    while (iterator1.hasNext() && flag1 == 0) {
                                        String key1 = (String) iterator1.next();
                                        if (elem.equals(key1)) {
                                            flag1 = 1;
                                            objTre.remove(key1);
                                            objTre.put(s1, "dir");

                                        }
                                    }
                                }
                            }
                            else if (key.equals(elem)&&flag3==0) {
                                flag3=1;
                                JSONArray a = objDue.getJSONArray(key);
                                objDue.remove(key);
                                objDue.put(s1, a);
                            }
                            if(flag2==1&&flag3==1)
                                flag=1;

                        }
                    }
                    //MODIFICO ALL'INTERNO DEL FILE CONTENENTE I FILE
                flag=0;
                    temp1 = leggiFile(FILE_FILES);
                    arrayTre = new JSONArray(temp1);
                    for (i = 0; i < arrayTre.length() && flag == 0; i++) {
                        //Estraggo l' oggetto json dall'array, in particolare l'i-esimo oggetto json
                       objUno= arrayTre.getJSONObject(i);
                        //ottengo le chiavi che ci sono nel mio array json
                        Iterator iterator = objUno.keys();
                        while (iterator.hasNext() && flag == 0) {
                            String key = (String) iterator.next();
                            if (elem.equals(key)) {
                                flag = 1;
                                JSONArray a = objUno.getJSONArray(key);
                                objUno.remove(key);
                                objUno.put(s1, a);
                            }

                        }
                    }


                scriviFile(arrayUno.toString(), FILE_DIRECTORIES);
                scriviFile(arrayTre.toString(), FILE_FILES);


            } else {

                temp = leggiFile(FILE_FILES);
                arrayUno=new JSONArray(temp);
                for (int index = 0; index < arrayUno.length() && flag == 0; index++) {
                    objDue = arrayUno.getJSONObject(index);
                    Iterator iterator = objDue.keys();
                    while (iterator.hasNext() && flag == 0) {
                        String key = (String) iterator.next();
                        if (key.equals(nomeCart)) {
                            flag = 1;
                            arrayDue = objDue.getJSONArray(key);
                            for (int j = 0; j < arrayDue.length() && flag1 == 0; j++) {
                                objTre= arrayDue.getJSONObject(j);
                                Iterator iterator1 = objTre.keys();
                                while (iterator1.hasNext() && flag1 == 0) {
                                    String key1 = (String) iterator1.next();

                                    if (elem.equals(key1)) {
                                        flag1 = 1;
                                        objTre.remove(key1);
                                        objTre.put(s1, "file");
                                    }
                                }
                            }
                        }
                    }
                }
                scriviFile(arrayUno.toString(), FILE_FILES);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }//RINOMINA L'ELEMENTO NELL'ARRAY JSON ESTERNO


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void elimina(ArrayList<SelectedItem> daEliminare, ArrayList<Integer> immagini, ArrayList<String> nomi, String nomeCart) {
        JSONArray parse;
        JSONArray parse1;
        JSONObject obj=new JSONObject();
        JSONObject obj_nuovo3=new JSONObject();
        JSONArray array_nuovo2=new JSONArray();
        JSONObject obj_nuovo2 = new JSONObject();
        int i, j, k, d;
        Myobject o = new Myobject();
        int flag = 0;
        int flag1 = 0;
        int flag2=0;
        int flag3=0;
        String temp, temp1;
        myAdapter.setData(immagini,nomi);
        try {
            for (SelectedItem sel : daEliminare) {

                d = sel.getPos();
                String msg = sel.getNome();
                Integer id = immagini.get(d);
                immagini.remove(d);
                nomi.remove(msg);
                //DEVO MODIFICARE QUI IL FILE
                //modificafile();
                //leggifile(FILE_DIRECTORIES);
                if (id.equals(R.drawable.ic_folder_grey)) {
                    temp1 = leggiFile(FILE_DIRECTORIES);
                    parse = new JSONArray(temp1);
                    for (k = 0; k < parse.length() && flag == 0; k++) {
                        obj_nuovo2 = parse.getJSONObject(k);
                        Iterator iterator = obj_nuovo2.keys();
                        while (iterator.hasNext() && flag == 0) {
                            String key = (String) iterator.next();
                            if (key.equals(nomeCart)&&flag2==0) {
                                flag2 = 1;
                                array_nuovo2 = obj_nuovo2.getJSONArray(key);
                                for (j = 0; j < array_nuovo2.length() && flag1 == 0; j++) {
                                    obj_nuovo3 = array_nuovo2.getJSONObject(j);
                                    Iterator iterator1 = obj_nuovo3.keys();
                                    while (iterator1.hasNext() && flag1 == 0) {
                                        String key1 = (String) iterator1.next();
                                        if (key1.equals(msg)) {
                                            flag1 = 1;
                                            obj_nuovo3.remove(key1);
                                            array_nuovo2.remove(j);
                                        }
                                    }
                                }
                            } else if (key.equals(msg)&&flag3==0) {
                                flag3 = 1;
                                obj_nuovo2.remove(key);
                                parse.remove(k);

                            }
                            if(flag2==1&&flag3==1)
                                flag=1;
                        }
                    }
                    flag=0;

                    //DEVO CANCELLARE ANCHE NEL FILE 2
                    temp1 = leggiFile(FILE_FILES);
                    parse1 = new JSONArray(temp1);
                    for (i = 0; i < parse1.length() && flag == 0; i++) {
                        //Estraggo l' oggetto json dall'array, in particolare l'i-esimo oggetto json
                        obj = parse1.getJSONObject(i);
                        //ottengo le chiavi che ci sono nel mio array json
                        Iterator iterator = obj.keys();
                        while (iterator.hasNext() && flag == 0) {
                            String key = (String) iterator.next();
                            if (msg.equals(key)) {
                                flag = 1;
                                obj.remove(key);
                                parse1.remove(i);
                            }

                        }
                    }
                    textView1.setText(parse.toString());
                    textView11.setText(parse1.toString());
                    scriviFile(parse.toString(), FILE_DIRECTORIES);
                    scriviFile(parse1.toString(), FILE_FILES);
                }

                //se voglio eliminare un file, leggo solo il file files e cerco
                else {

                    temp = leggiFile(FILE_FILES);
                    parse = new JSONArray(temp);
                    for (int index = 0; index < parse.length() && flag == 0; index++) {
                        obj_nuovo2 = parse.getJSONObject(index);
                        Iterator iterator = obj_nuovo2.keys();
                        while (iterator.hasNext() && flag == 0) {
                            String key = (String) iterator.next();
                            if (key.equals(nomeCart)) {
                                flag = 1;
                                array_nuovo2 = obj_nuovo2.getJSONArray(key);
                                for (j = 0; j < array_nuovo2.length() && flag1 == 0; j++) {
                                    obj_nuovo3 = array_nuovo2.getJSONObject(j);
                                    Iterator iterator1 = obj_nuovo3.keys();
                                    while (iterator1.hasNext() && flag1 == 0) {
                                        String key1 = (String) iterator1.next();
                                        if (msg.equals(key1)) {
                                            flag1 = 1;
                                            obj_nuovo3.remove(key1);
                                            array_nuovo2.remove(j);

                                        }
                                    }
                                }
                            }
                        }
                    }
                    textView11.setText(parse.toString());
                    scriviFile(parse.toString(), FILE_FILES);
                }
            }daEliminare.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String leggiFile(File f) {
        FileInputStream stream;
        String temp = "";
        int c;
        try {
            stream = new FileInputStream(f);
            while ((c = stream.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }

            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public void scriviFile(String scrivi, File f) {
        FileOutputStream fOut1;
        try {

            fOut1 = new FileOutputStream(f);
            fOut1.write(scrivi.getBytes());
            fOut1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseJson(ArrayList<Integer> immagini, ArrayList<String> nomi, String nomeCart) {
        JSONArray arrayUno, arrayDue;
        JSONObject objUno = new JSONObject();
        JSONObject objDue = new JSONObject();
        int i, j;
        reading = leggiFile(FILE_DIRECTORIES);
        reading2 = leggiFile(FILE_FILES);
       // myAdapter.setData(immagini,nomi);
        nomi.add("..");
        immagini.add(R.drawable.ic_folder_grey);


        try {
            arrayUno = new JSONArray(reading);
                for (i = 0; i < arrayUno.length(); i++) {
                    objUno = arrayUno.getJSONObject(i);
                    Iterator iterator = objUno.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        if (key.equals(nomeCart))
                        {  arrayDue = objUno.getJSONArray(key);
                          for (j = 0; j < arrayDue.length(); j++) {
                                    objDue= arrayDue.getJSONObject(j);
                                    Iterator iterator2 = objDue.keys();
                                    while (iterator2.hasNext()) {
                                        String key2 = (String) iterator2.next();
                                        nomi.add(key2);
                                        immagini.add(R.drawable.ic_folder_grey);

                        }
                    }

            }}}

            //ADESSO FACCIO IL PARSE DEL FILE CONTENENTE I FILES
           arrayUno = new JSONArray(reading2);
            for (i = 0; i < arrayUno.length(); i++) {
                objUno = arrayUno.getJSONObject(i);
                Iterator iterator1 = objUno.keys();
                while (iterator1.hasNext()) {
                    String key1 = (String) iterator1.next();
                    if (key1.equals(nomeCart)) {
                       arrayDue = objUno.getJSONArray(key1);
                        for (j = 0; j < arrayDue.length(); j++) {
                            objDue = arrayDue.getJSONObject(j);
                            Iterator iterator2 = objDue.keys();
                            while (iterator2.hasNext()) {
                                String key2 = (String) iterator2.next();
                                nomi.add(key2);
                                immagini.add(R.drawable.ic_file_blue);
                            }
                        }
                    }
                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

//POSSO FARE LA LETTURA DEL FILE ANCHE COSÌ,
/*
*   try {
            stream = new FileInputStream(yourFile);

            try {
                fc = stream.getChannel();
                bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
                tv.setText(jsonStr);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/













