package com.giulia.floatingactionbutton;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by giulia on 06/10/17.
 */

public class Fragment1 extends Fragment {
    File FILE_PATH_SDCARD = Environment.getExternalStorageDirectory();
    File FILE_DIRECTORIES = new File(FILE_PATH_SDCARD, "Documents/l.json");
    File FILE_FILES = new File(FILE_PATH_SDCARD, "Documents/l1.json");

    ListView lv;
    Myadapter myadapter;
    String nomeCartella;
    TextView textView0;
    ArrayList<String> currentPath;
    String currentDir;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.first_fragment,container,false);
        lv=(ListView) rootView.findViewById(R.id.listView1);
        textView0=(TextView)rootView.findViewById(R.id.textView0);
        myadapter=new Myadapter(getActivity(),new ArrayList<Integer>(),new ArrayList<String>());
        lv.setAdapter(myadapter);
        currentPath = new ArrayList<>();
        currentPath.add(nomeCartella);
        currentDir = "/" + currentPath.get(0);
        textView0.setText(currentDir);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String displayPath = "";
                nomeCartella = (String) lv.getItemAtPosition(position);
                Integer id1 = myadapter.getImageId(position);
                if (id1.equals(R.drawable.ic_folder_grey)) {


                    if (nomeCartella.equals("..")) {
                        currentPath.remove(currentPath.size() - 1);
                        if (currentPath.size() == 0) {


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
                    myadapter.setData(new ArrayList<Integer>(), new ArrayList<String>());
                    parseJson(new ArrayList<Integer>(), new ArrayList<String>(), nomeCartella);
            }}
        });


      return super.onCreateView(inflater, container, savedInstanceState);
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
    public void parseJson(ArrayList<Integer> immagini, ArrayList<String> nomi, String nomeCartella) {
        JSONArray parse, arrayjson2;
        JSONObject obj = new JSONObject();
        JSONObject obj1 = new JSONObject();
        int i, j;
        String reading = leggiFile(FILE_DIRECTORIES);
        String reading2 = leggiFile(FILE_FILES);
        nomi.add("..");
        immagini.add(R.drawable.ic_folder_grey);


        try {
            parse = new JSONArray(reading);
            //SE LA CARTELLA SU CUI HO CLICCATO È LA CARTELLA PRINCIPALE
            if (nomeCartella.equals("Tutti i file")) {

                for (i = 0; i < parse.length(); i++) {
                    obj = parse.getJSONObject(i);
                    Iterator iterator = obj.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        if (!key.equals("Tutti i file")) {
                            nomi.add(key);
                            immagini.add(R.drawable.ic_folder_grey);
                        }
                    }
                }
            } else { //SE INVECE LA CARTELLA SU CUI HO CLICCATO È UNA QUALSIASI ALTRA CARTELLA
                //DEVO GUARDARE NELL'ARRAY CORRISPONDENTE ALLA CHIAVE DELLA CARTELLA

                for (i = 0; i < parse.length(); i++) {
                    obj = parse.getJSONObject(i);
                    Iterator iterator1 = obj.keys();
                    while (iterator1.hasNext()) {
                        String key1 = (String) iterator1.next();
                        if (key1.equals(nomeCartella)) {
                            arrayjson2 = obj.getJSONArray(key1);
                            for (j = 0; j < arrayjson2.length(); j++) {
                                obj1 = arrayjson2.getJSONObject(j);
                                Iterator iterator2 = obj1.keys();
                                while (iterator2.hasNext()) {
                                    String key2 = (String) iterator2.next();
                                    nomi.add(key2);
                                    immagini.add(R.drawable.ic_folder_grey);
                                }
                            }
                        }


                    }
                }
            }

            //ADESSO FACCIO IL PARSE DEL FILE CONTENENTE I FILES

            parse = new JSONArray(reading2);
            for (i = 0; i < parse.length(); i++) {
                obj = parse.getJSONObject(i);
                Iterator iterator1 = obj.keys();
                while (iterator1.hasNext()) {
                    String key1 = (String) iterator1.next();
                    if (key1.equals(nomeCartella)) {
                        arrayjson2 = obj.getJSONArray(key1);
                        for (j = 0; j < arrayjson2.length(); j++) {
                            obj1 = arrayjson2.getJSONObject(j);
                            Iterator iterator2 = obj1.keys();
                            while (iterator2.hasNext()) {
                                String key2 = (String) iterator2.next();
                                nomi.add(key2);
                                immagini.add(R.drawable.ic_file_blue);
                            }
                        }
                    }
                }

                myadapter.setData(immagini, nomi);
                myadapter.notifyDataSetChanged();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

