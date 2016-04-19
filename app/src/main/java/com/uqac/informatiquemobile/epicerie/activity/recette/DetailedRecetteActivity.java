package com.uqac.informatiquemobile.epicerie.activity.recette;

import android.app.Activity;
import android.content.Intent;
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
    private Recette recette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailed_recette_activity);

        int id = getIntent().getExtras().getInt("id");
        recette=displayinfo(id);

        buttonMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idRecette = recette.getId();
                Intent mIntent = new Intent(getApplicationContext(), ModifyRecetteActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("id", idRecette);
                mIntent.putExtras(extras);
                startActivityForResult(mIntent, 456);
            }
        });
        buttonDel.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                int id = getIntent().getExtras().getInt("id");

                dbm.deleteRecette(id);


                finish();
            }

        });

    }

    private Recette displayinfo(int id){
        dbm = new DataBaseManager(getApplicationContext());
        final Recette recette= dbm.getRecetteById(id);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewDesc=(TextView) findViewById(R.id.textViewDesc);

        buttonMod = (Button) findViewById(R.id.buttonMod);
        buttonDel = (Button) findViewById(R.id.buttonDel);
        listViewIngredients = (ListView)findViewById(R.id.listViewIngredients);

        textViewName.setText(recette.getNom());
        textViewDesc.setText("composition.size : "+recette.getComposition().size()+" \nmissing : "+dbm.recetteIsAvailable(recette));

        ingredients = new ArrayList(recette.getComposition());

        adapter = new IngredientListAdapter(getApplicationContext(), R.layout.resultat_recherche_layout, (List)ingredients, true);
        listViewIngredients.setAdapter(adapter);
        return recette;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null){
            if(requestCode == 456){



                Bundle extras = data.getExtras();
                if (extras != null) {
                    int id =extras.getInt("id");
                    recette=displayinfo(id);
                }




            }
        }
    }
}