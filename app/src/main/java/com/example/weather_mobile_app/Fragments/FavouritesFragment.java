package com.example.weather_mobile_app.Fragments;

import static com.example.weather_mobile_app.MainActivity.JSON_CURRENT;
import static com.example.weather_mobile_app.MainActivity.JSON_FORECAST;

import android.app.Dialog;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.weather_mobile_app.AppConfig;
import com.example.weather_mobile_app.Interfaces.CityNameListener;
import com.example.weather_mobile_app.MainActivity;
import com.example.weather_mobile_app.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FavouritesFragment extends Fragment {
    private static Dialog dialog;
    private static ArrayList<String> arrayList;
    private static TextView currentLoc;
    private static ListView locs;
    private static ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites_main, container, false);
        arrayList = MainActivity.getMainActivity().recoverFavList();
        prepareComponents(view);
        return view;
    }

    private static void prepareComponents(View view) {
        currentLoc = view.findViewById(R.id.tvCurrentLoc);
        updateCurrentLocalization();

        locs = (ListView) view.findViewById(R.id.lvLocs);
        locs.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter = new ArrayAdapter<>(MainActivity.getMainActivity(), android.R.layout.simple_list_item_1, arrayList);
        locs.setAdapter(adapter);
        locs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                parent.getChildAt(savedItemPos).setBackgroundColor(Color.TRANSPARENT);
//                view.setBackgroundColor(Color.GREEN);
//                Log.i("ITEMEK", ((TextView)view).getText().toString());
                String current = ((TextView)view).getText().toString();
                AppConfig.setCurrentLoc(current);
                currentLoc.setText(current);
                MainActivity.getMainActivity().getAPIData();
            }
        });

        ImageView ivAdd = (ImageView) view.findViewById(R.id.ivAdd);
        dialogAdd(ivAdd);

        ImageView ivDel = (ImageView) view.findViewById(R.id.ivDelete);
        dialogDelete(ivDel);

    }

    private static void updateCurrentLocalization() {
        String curr = AppConfig.getCurrentLoc();
        if (curr != null) {
            currentLoc.setText(AppConfig.getCurrentLoc());
        }
//        else {
//            currentLoc.setText("Not set");
//        }
    }

    private static void dialogAdd(ImageView ivAdd) {
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(MainActivity.getMainActivity());

                dialog.setContentView(R.layout.dialog_add);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();


                EditText editText=dialog.findViewById(R.id.edit_text);
                Button add = dialog.findViewById(R.id.addButton);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!editText.getText().toString().isEmpty()) {
                            String newItem = editText.getText().toString();

                            MainActivity.getMainActivity().getCityNameFromApi(newItem, new CityNameListener() {
                                @Override
                                public void onCityNameReceived(String cityName) {
                                    if (cityName.equals("0")) {
                                        MainActivity.writeToast("Wrong city!");
                                        return;
                                    }
                                    arrayList.add(cityName);
                                    adapter.notifyDataSetChanged();

                                    MainActivity.writeToast(cityName + " added!");
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            MainActivity.writeToast("Empty field!");
                        }
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

                dialog.setContentView(R.layout.dialog_delete);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                        String deletedItem = arrayList.get(position);
                        Log.i("DELETE", "DELETING: " + deletedItem);
                        if (Objects.equals(deletedItem, AppConfig.getCurrentLoc())) {
                            AppConfig.setCurrentLoc(null);
                            updateCurrentLocalization();
                        }
                        MainActivity.getMainActivity().removeDataJSON(JSON_CURRENT, deletedItem);
                        MainActivity.getMainActivity().removeDataJSON(JSON_FORECAST, deletedItem);

                        arrayList.remove(position);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();

                        MainActivity.writeToast(deletedItem + " deleted!");
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
    public static String getSetLoc() {
        return currentLoc.getText().toString();
    }

    public static Set<String> arrayListToSet() {
        // Tworzymy nowy HashSet i dodajemy wszystkie elementy z ArrayList do niego
        return new HashSet<>(arrayList);
    }
}
