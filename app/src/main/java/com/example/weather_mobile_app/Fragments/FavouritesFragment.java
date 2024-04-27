package com.example.weather_mobile_app.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weather_mobile_app.MainActivity;
import com.example.weather_mobile_app.R;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {
    private static Dialog dialog;
    private static ArrayList<String> arrayList;
    private TextView currentLoc;
    private static ListView locs;
    static ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites_main, container, false);
        arrayList=new ArrayList<>();
        arrayList.add("Warszawa");
        arrayList.add("Poznan");
        arrayList.add("Krakow");
        arrayList.add("Washington");
        arrayList.add("Prague");
        arrayList.add("Londyn");
        arrayList.add("Berlin");
        arrayList.add("Amriswil");
        prepareComponents(view);
        return view;
    }

    // listview zamiast przyciskow
    // dwa przyciski, jeden dodaje, drugi usuwa

    private static void prepareComponents(View view) {
        locs = (ListView) view.findViewById(R.id.lvLocs);
        adapter = new ArrayAdapter<>(MainActivity.getMainActivity(), android.R.layout.simple_list_item_1,arrayList);
        locs.setAdapter(adapter);


        ImageView ivAdd = (ImageView) view.findViewById(R.id.ivAdd);
        dialogAdd(ivAdd);

        ImageView ivDel = (ImageView) view.findViewById(R.id.ivDelete);
        dialogDelete(ivDel);

    }

    private static void dialogAdd(ImageView ivAdd) {
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(MainActivity.getMainActivity());

                // set custom dialog
                dialog.setContentView(R.layout.dialog_add);

                // set custom height and width
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();


                EditText editText=dialog.findViewById(R.id.edit_text);
                Button add = dialog.findViewById(R.id.addButton);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("BUTTON_ADD", "CLICK");
                        if (!editText.getText().toString().isEmpty()) {
                            arrayList.add(editText.getText().toString());
                            adapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancelAdd);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private static void dialogDelete(ImageView button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(MainActivity.getMainActivity());

                // set custom dialog
                dialog.setContentView(R.layout.dialog_delete);

                // set custom height and width
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                ListView listView=dialog.findViewById(R.id.list_view);

                // Initialize array adapter
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getMainActivity(), android.R.layout.simple_list_item_1,arrayList);

                // set adapter
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
//                        textview.setText(adapter.getItem(position));
                        Log.i("DELETE", "DELETING: " + arrayList.get(position));
                        arrayList.remove(position);
                        adapter.notifyDataSetChanged();
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancelDelete);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
