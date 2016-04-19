package com.uqac.informatiquemobile.epicerie.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.uqac.informatiquemobile.epicerie.adapter.IngredientListAdapter;
import com.uqac.informatiquemobile.epicerie.R;
import com.uqac.informatiquemobile.epicerie.dataBase.DataBaseManager;
import com.uqac.informatiquemobile.epicerie.metier.Nourriture;
import com.uqac.informatiquemobile.epicerie.metier.Recette;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume2 on 30/03/2016.
 */
public class DetailedRecetteActivity extends Activity {
    private DataBaseManager dbm ;
    private ListView listViewIngredients;
    private TextView textViewName;
    private TextView textViewDesc;
    private Button buttonMod;
    private Button buttonDel;
    private ArrayList<Nourriture> ingredients;
    private IngredientListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailed_recette_activity);

        int id = getIntent().getExtras().getInt("id");
        dbm = new DataBaseManager(getApplicationContext());
        Recette recette= dbm.getRecetteById(id);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewDesc=(TextView) findViewById(R.id.textViewDesc);

        buttonMod = (Button) findViewById(R.id.buttonMod);
        buttonDel = (Button) findViewById(R.id.buttonDel);
        listViewIngredients = (ListView)findViewById(R.id.listViewIngredients);

        textViewName.setText(recette.getNom());
        textViewDesc.setText("composition.size : "+recette.getComposition().size()+" \nmissing : "+dbm.RecetteIsAvailable(recette));

        ingredients = new ArrayList(recette.getComposition());

        adapter = new IngredientListAdapter(getApplicationContext(), R.layout.resultat_recherche_layout, (List)ingredients, true);
        listViewIngredients.setAdapter(adapter);

        buttonMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonDel.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                int id = getIntent().getExtras().getInt("id");

                dbm.DeleteRecette(id);


                finish();
            }

        });

    }
}
