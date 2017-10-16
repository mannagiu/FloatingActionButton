package com.giulia.floatingactionbutton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class SecondPage extends AppCompatActivity {
    ArrayList<Integer> arrayImages, arrayImages1;
    //ArrayList con il nome delle cartelle e/o file:
    ArrayList<String> arrayItemNames, arrayItemNames1;
    //Gli array sopra dichiarati mi servono per riempire la listview


    //Struttura dati b-albero per tenere in memoria le cartelle ecc

    ArrayList<String> currentPath;
    TextView textView1, textView11, textView0;
    ListView listView1;
    File FILE_PATH_SDCARD = Environment.getExternalStorageDirectory();
   // File FILE_DIRECTORIES = new File(FILE_PATH_SDCARD, "Documents/nuevo.json");
    //File FILE_FILES = new File(FILE_PATH_SDCARD, "Documents/nuovo1.json");
    File fileDir,fileFile;
    FloatingActionButton fAb,fAb2;
    FloatingActionsMenu floatingActionsMenu;
    Bundle datoPassato;
    Bundle richiesta;

    String currentDir,  reading, reading2;
    String nomeCartella,choosedFile,ric,fileName;
       Myadapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        datoPassato=getIntent().getExtras();
       richiesta=getIntent().getExtras();
       choosedFile=datoPassato.getString("choosed file");

            fileDir = new File(FILE_PATH_SDCARD, "Documents/"+choosedFile+".json");




        listView1 = (ListView) findViewById(R.id.listView1);
        textView0 = (TextView) findViewById(R.id.textView0);
        nomeCartella = "Tutti i file";
        arrayImages=new ArrayList<>();
        arrayItemNames = new ArrayList<>();

        textView1 = (TextView) findViewById(R.id.textView1);
        //textView11 = (TextView) findViewById(R.id.textView11);


        fAb=(FloatingActionButton)findViewById(R.id.addfolder);
        fAb2=(FloatingActionButton)findViewById(R.id.addfile);
        floatingActionsMenu=(FloatingActionsMenu)findViewById(R.id.menu);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                fAb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SecondPage.this);
                    final View mView = getLayoutInflater().inflate(R.layout.dialog_newfolder, null);
                    final EditText cartella = (EditText) mView.findViewById(R.id.nome_cartella);

                    mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!cartella.getText().toString().isEmpty()) {
                                Toast.makeText(SecondPage.this, "Cartella aggiunta", Toast.LENGTH_SHORT).show();
                                String r = cartella.getText().toString();
                                create(arrayImages,arrayItemNames, r, nomeCartella,"dir");


                                ;
                                //textview.setText(str);

                            }
                            floatingActionsMenu.collapse();

                        }
                    });
                    mBuilder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            floatingActionsMenu.collapse();


                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();



                }
            });

                fAb2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(SecondPage.this);
                        final View mView1 = getLayoutInflater().inflate(R.layout.dialog_newfile, null);
                        final EditText file = (EditText) mView1.findViewById(R.id.nomefile);

                        mBuilder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!file.getText().toString().isEmpty()) {
                                    Toast.makeText(SecondPage.this, "File aggiunto", Toast.LENGTH_SHORT).show();
                                    String r1 = file.getText().toString();
                                    create(arrayImages,arrayItemNames, r1,nomeCartella,"file");

                                }
                                floatingActionsMenu.collapse();


                            }
                        });


                        mBuilder1.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                floatingActionsMenu.collapse();

                            }
                        });


                        mBuilder1.setView(mView1);
                        AlertDialog dialog1 = mBuilder1.create();
                        dialog1.show();


                    }
                });
            }




            @Override
            public void onMenuCollapsed() {

            }
        });


        //customListViewAdapter=new CustomListViewAdapter(SecondPage.this, R.layout.items_list,listRowItems);
        myAdapter=new Myadapter(SecondPage.this,arrayImages,arrayItemNames);
        listView1.setAdapter(myAdapter);
        currentPath = new ArrayList<>();
        currentPath.add(nomeCartella);
        currentDir = "/" + currentPath.get(0);
        textView0.setText(currentDir);


        reading = readFile(fileDir);
        //reading2 = readFile(fileFile);
        textView1.setText(reading);
        parseJson(arrayImages,arrayItemNames, nomeCartella);
        registerForContextMenu(listView1);


       // textView1.setText(reading);
        //textView11.setText(reading2);



        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                String displayPath = "";
                nomeCartella = (String) listView1.getItemAtPosition(pos);

                Integer id1 = myAdapter.getImageId(pos);
               if (id1.equals(R.drawable.ic_arrow_back_black_24dp)) {

                        currentPath.remove(currentPath.size() - 1);

                        if (currentPath.size() == 0) {

                            Intent intent = new Intent(SecondPage.this, Radice.class);
                            startActivity(intent);
                            finish();

                        } else
                            nomeCartella = currentPath.get(currentPath.size() - 1);


                    } else if(id1.equals(R.drawable.ic_folder_grey)) {


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
                    parseJson(arrayImages,arrayItemNames, nomeCartella);

            }
        });
       /* listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
              SelectedItem s1=new SelectedItem(position,listView1.getItemAtPosition(position).toString());

               arraySelectedItems.add(s1);
                return true;
            }
        });*/

        listView1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        listView1.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                final int checkedCount = listView1.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                myAdapter.toggleSelection(position);
                //arraySelectedItems.add(myAdapter.getItem(position));
                /*SelectedItem s=new SelectedItem(position,myAdapter.getItem(position));

                if(checked)
             {

                 arraySelectedItems.add(s);
                 count++;
                 mode.setTitle(count + "elementi selezionati");
             }
             else
             {   arraySelectedItems.remove(s);
                 //selection.remove(listView1.getItemAtPosition(position));
                 count--;
                 mode.setTitle(count + "elementi selezionati");
             }*/
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
                final int checkedCount = listView1.getCheckedItemCount();

                switch (checkedCount) {
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
                        int count=0;
                        // call getSelectedIds method from customListViewAdapter
                        SparseBooleanArray selected = myAdapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                String mia =  myAdapter.getItem(selected.keyAt(i));
                                // Remove selected items using ids
                                int a=myAdapter.getItemPosition(mia);
                                if(myAdapter.getImageId(a).equals(R.drawable.ic_arrow_back_black_24dp))
                                {Toast.makeText(getApplicationContext(),"Impossibile eliminare l'elemento <---",Toast.LENGTH_LONG).show();}
                                else
                                {count++;
                                delete(mia,nomeCartella);
                                myAdapter.remove(mia);
                                    Toast.makeText(getBaseContext(), count + "elementi eliminati", Toast.LENGTH_SHORT).show();
                            }}
                        }





                        // elimina(arraySelectedItems, arrayImages, arrayItemNames, nomeCartella);

                        myAdapter.removeSelection();
                        mode.finish();
                        return true;

                    case R.id.rename:
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SecondPage.this);
                        final View mView = getLayoutInflater().inflate(R.layout.dialog_rename, null);
                        final EditText rinomina = (EditText) mView.findViewById(R.id.rinomina_cartella);
                        final SparseBooleanArray selecte = myAdapter.getSelectedIds();

                            if (selecte.valueAt(selecte.size()-1)) {

                                final String mia = myAdapter.getItem(selecte.keyAt(0));
                                int a = myAdapter.getItemPosition(mia);
                                if (myAdapter.getImageId(a).equals(R.drawable.ic_arrow_back_black_24dp)) {
                                    Toast.makeText(getApplicationContext(), "Impossibile rinominare l'elemento", Toast.LENGTH_LONG).show();
                                    myAdapter.removeSelection();
                                    return true;
                                } else

                                {

                                    mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!rinomina.getText().toString().isEmpty()) {
                                                Toast.makeText(SecondPage.this, "Elemento rinominato", Toast.LENGTH_SHORT).show();
                                                String s2 = rinomina.getText().toString();
                                                // Captures all selected ids with a loop
                                                rename(mia, s2, nomeCartella);
                                                myAdapter.rename(myAdapter.getItemPosition(mia), s2);
                                            }

                                /*
                                    String myAdapterItem=arraySelectedItems.get(0);
                                    rename(myAdapterItem,s2,nomeCartella);
                                    myAdapter.rename(myAdapter.getItemPosition(arraySelectedItems.get(0)),s2);
                                    arraySelectedItems.clear();
                                                                                */


                                            myAdapter.removeSelection();

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
                                }
                            }
                    default:return false;
                }


            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                //myAdapter.removeSelection();
              // arraySelectedItems.clear();

            }
        });
        fAb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SecondPage.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_newfolder, null);
                final EditText cartella = (EditText) mView.findViewById(R.id.nome_cartella);

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!cartella.getText().toString().isEmpty()) {
                            Toast.makeText(SecondPage.this, "Cartella aggiunta", Toast.LENGTH_SHORT).show();
                            String r = cartella.getText().toString();
                            create(arrayImages,arrayItemNames, r, nomeCartella,"dir");

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


            }
        });

        fAb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(SecondPage.this);
                final View mView1 = getLayoutInflater().inflate(R.layout.dialog_newfile, null);
                final EditText file = (EditText) mView1.findViewById(R.id.nomefile);

                mBuilder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!file.getText().toString().isEmpty()) {
                            Toast.makeText(SecondPage.this, "File aggiunto", Toast.LENGTH_SHORT).show();
                            String r1 = file.getText().toString();
                            create(arrayImages,arrayItemNames, r1,nomeCartella,"file");

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
        });
    }




    //end of OnCreate


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
                            create(arrayImages,arrayItemNames, r, nomeCartella,"dir");

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
                            create(arrayImages,arrayItemNames, r1,nomeCartella,"file");

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


    private void create(ArrayList<Integer> immagini, ArrayList<String> nomi, String r1, String nomeCart,String tipo) {
        JSONObject obj_nuovo1, obj_nuovo2, obj_nuovo3;
        JSONArray array_nuovo1, array_nuovo2, parse;
        obj_nuovo1 = new JSONObject();
        array_nuovo1 = new JSONArray();
        obj_nuovo2 = new JSONObject();
        obj_nuovo3 = new JSONObject();
        array_nuovo2 = new JSONArray();
        parse=new JSONArray();
        String name;

        String risultato = readFile(fileDir);
        int flag = 0;
        //Creare un nuovo oggetto e aggiungerlo all'albero
        //Myobject o = new Myobject(r, "dir", false);
        //arrayMyObjects.add(o);
        //AGGIUNGO ALLA LISTVIEW GLI ELEMENTI
       // myAdapter.setData(immagini,nomi);

        nomi.add(r1);
        if(tipo.equals("dir"))
            immagini.add(R.drawable.ic_folder_grey);
        else
            immagini.add(R.drawable.ic_file_blue);
        //CREO L'OGGETTO JSON CORRISPONDENTE ALLA SCRITTURA
        /*
        * Devo creare un oggetto json da inserire nell'array json principale
        * e uno da inserire nell'array corrispondente alla cartella dove voglio creare il file
        * */
        try {
            parse = new JSONArray(risultato);
            obj_nuovo3.put("nome",r1);
            obj_nuovo3.put("tipo", tipo);

            for (int i = 0; i < parse.length()&&flag==0; i++) {
                obj_nuovo2 = parse.getJSONObject(i);
               name = obj_nuovo2.getString("nome");
                if (name.equals(nomeCart)) {
                    flag = 1;
                    array_nuovo2 = obj_nuovo2.getJSONArray("figli");
                    array_nuovo2.put(obj_nuovo3);
                }

            }
                /*Iterator iterator = obj_nuovo2.keys();
                while (iterator.hasNext() && flag == 0) {
                    String key = (String) iterator.next();
                    if (key.equals(nomeCart)) {
                        flag = 1;
                        array_nuovo2 = obj_nuovo2.getJSONArray(key);
                        array_nuovo2.put(obj_nuovo3);

                    }
            }*/


            /*Se non ho trovato la cartella tra gli oggetti json principali
            significa che la cartella era vuota,la devo creare e inserisco la sottocartella
             */
            if(flag==0)
            {
                obj_nuovo3.put("nome",r1);
                obj_nuovo3.put("tipo",tipo);
                array_nuovo1.put(obj_nuovo3);
                obj_nuovo1.put("figli",array_nuovo1);
                obj_nuovo1.put("nome",nomeCart);
                obj_nuovo1.put("tipo","dir");
                parse.put(obj_nuovo1);
            }

            risultato = parse.toString();
            textView1.setText(risultato);
            writeFile(risultato, fileDir);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rename( String elem, String s1, String nomeCart) { //devo passare l'array contenente i miei oggetti
       String temp, temp1,tipo;

        int flag = 0;
        int flag1 = 0;
        int flag3=0;


        JSONArray arrayUno;
        JSONArray arrayDue=new JSONArray();
        JSONArray arrayTre;
        JSONObject objUno= new JSONObject();
        JSONObject objDue=new JSONObject();
        JSONObject objTre=new JSONObject();

        //AZZERO LA LISTA DEI SELEZIONATI
        try {

            Integer im=myAdapter.getImageId(myAdapter.getItemPosition(elem));
            if (im.equals(R.drawable.ic_folder_grey))
                tipo="dir";
            else
                tipo="file";

            {
                temp = readFile(fileDir);
                arrayUno = new JSONArray(temp);
                for (int index = 0; index < arrayUno.length() && flag == 0; index++) {
                    objDue = arrayUno.getJSONObject(index);
                    if (objDue.getString("nome").equals(nomeCart)) {
                        //accedi all'array figli
                        arrayDue = objDue.getJSONArray("figli");
                        for (int j = 0; j < arrayDue.length() && flag1 == 0; j++) {
                            objTre = arrayDue.getJSONObject(j);
                            if (objTre.getString("nome").equals(elem)) {
                                flag1 = 1;
                                objTre.remove("nome");
                                objTre.put("nome", s1);
                            }

                        }

                    } else if (objDue.getString("nome").equals(elem) && tipo.equals("dir")) {
                        flag3 = 1;
                        JSONArray a = objDue.getJSONArray("figli");
                        objDue.remove("nome");
                        objDue.put("nome", s1);
                    }
                    if (flag1 == 1 && flag3 == 1)
                        flag = 1;

                }


                //MODIFICO ALL'INTERNO DEL FILE CONTENENTE I FILE

                writeFile(arrayUno.toString(), fileDir);

                 textView1.setText(arrayUno.toString());
                //textView11.setText(arrayTre.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();

        }//RINOMINA L'ELEMENTO NELL'ARRAY JSON ESTERNO


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void delete(String msg, String nomeCart) {
        String temp, temp1,tipo;

        int flag = 0;
        int flag1 = 0;
        int flag3=0;


        JSONArray arrayUno;
        JSONArray arrayDue=new JSONArray();
        JSONArray arrayTre;
        JSONObject objUno= new JSONObject();
        JSONObject objDue=new JSONObject();
        JSONObject objTre=new JSONObject();
        JSONObject objQuattro=new JSONObject();

        //AZZERO LA LISTA DEI SELEZIONATI
        try {

            Integer im=myAdapter.getImageId(myAdapter.getItemPosition(msg));
            if (im.equals(R.drawable.ic_folder_grey))
                tipo="dir";
            else
                tipo="file";


                temp = readFile(fileDir);
                arrayUno = new JSONArray(temp);
                for (int index = 0; index < arrayUno.length() && flag == 0; index++) {
                    objDue = arrayUno.getJSONObject(index);
                    if (objDue.getString("nome").equals(nomeCart)) {
                        //accedi all'array figli
                        arrayDue = objDue.getJSONArray("figli");
                        for (int j = 0; j < arrayDue.length() && flag1 == 0; j++) {
                            objTre = arrayDue.getJSONObject(j);
                            if (objTre.getString("nome").equals(msg)) {
                                flag1 = 1;
                                arrayDue.remove(j);

                            }

                        }

                    } else if (objDue.getString("nome").equals(msg) && tipo.equals("dir")) {
                        flag3 = 1;
                        arrayTre=objDue.getJSONArray("figli");

                        //devo andare a vedere se questa cartella che voglio eliminare
                        //aveva qualcosa al suo interno ed eliminare tutto quello che aveva
                        arrayUno.remove(index);
                        /* in questo caso devo salvare il contenuto di questa cartella
                            ed eliminarla dalla struttura dati, anche se eliminando direttamente
                            il nodo il problema non si pone, me la canto e me la suono
                         */
                        for(int i=0;i<arrayTre.length();i++) {
                            objQuattro=arrayTre.getJSONObject(i);
                            delete(objQuattro.getString("nome"),msg);
                        }
                    }
                    if (flag1 == 1 && flag3 == 1)
                        flag = 1;

                }



                //MODIFICO ALL'INTERNO DEL FILE CONTENENTE I FILE

                writeFile(arrayUno.toString(), fileDir);

                textView1.setText(arrayUno.toString());
                //textView11.setText(arrayTre.toString());


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private String readFile(File f) {
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

    public void writeFile(String scrivi, File f) {
        FileOutputStream fOut1;
        try {

            fOut1 = new FileOutputStream(f);
            fOut1.write(scrivi.getBytes());
            fOut1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseJson(ArrayList<Integer> immagini,ArrayList<String> nomi, String nomeCart) {
        JSONArray arrayUno, arrayDue;
        JSONObject objUno = new JSONObject();
        JSONObject objDue = new JSONObject();
        int i, j;
        int flag=0;
        reading = readFile(fileDir);
       // myAdapter.setData(immagini,nomi);
       // items.add(item);
        nomi.add("..");
        immagini.add(R.drawable.ic_arrow_back_black_24dp);


        try {
            arrayUno = new JSONArray(reading);
                for (i = 0; i < arrayUno.length()&&flag==0; i++) {
                    objUno = arrayUno.getJSONObject(i);
                    if(objUno.getString("nome").equals(nomeCart))
                    {   flag=1;
                        arrayDue=objUno.getJSONArray("figli");
                    for (j = 0; j < arrayDue.length(); j++) {
                        objDue=arrayDue.getJSONObject(j);
                        nomi.add(objDue.getString("nome"));
                        if(objDue.getString("tipo").equals("dir"))
                            immagini.add(R.drawable.ic_folder_grey);
                        else
                            immagini.add(R.drawable.ic_file_blue);

                    }}}

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

