package com.uqac.informatiquemobile.epicerie.activity.ingredient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uqac.informatiquemobile.epicerie.R;
import com.uqac.informatiquemobile.epicerie.adapter.IngredientListAdapter;
import com.uqac.informatiquemobile.epicerie.dataBase.DataBaseManager;
import com.uqac.informatiquemobile.epicerie.metier.Ingredient;

import java.util.ArrayList;

public class ListFrigoActivity extends AppCompatActivity {

    private DataBaseManager dbm;

    private ListView listViewIngredients;
    private ArrayList<Ingredient> listIngredients;

    private IngredientListAdapter adapterListViewIngredients;

    private TextView textViewValeurIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ingredients_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                Intent i = new Intent(getApplicationContext(), AddIngredientActivity.class);
                startActivityForResult(i, 123);
            }
        });



        dbm = new DataBaseManager(getApplicationContext());
        //dbm.viderIngredient();
        //dbm.FixtureIngredients();

        listViewIngredients = (ListView) findViewById(R.id.listIngredients);
        listIngredients = new ArrayList<>();

        listIngredients= dbm.getAllIngredientFrigo();


        adapterListViewIngredients = new IngredientListAdapter(ListFrigoActivity.this, R.layout.resultat_recherche_layout, listIngredients, false);
        listViewIngredients.setAdapter(adapterListViewIngredients);


        listViewIngredients.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //final String tv = ((TextView)view).getText().toString();
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(ListFrigoActivity.this);
                View promptsView = li.inflate(R.layout.input_quantite_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ListFrigoActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                final int POS = position;

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Supprimer",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //Ingredient temp = dbm.getIngredientByNm(tv);
                                        if(userInput.getText().toString().length()!=0){
                                            float resultat = Float.parseFloat(userInput.getText().toString());



                                                System.out.println("WOLOLO");


                                                Ingredient ingredient=listIngredients.get(POS);
                                                dbm.supprimerIngredientFrigo(ingredient, resultat);
                                                Toast.makeText(getApplicationContext(), "Delete : " + ingredient.getNom(), Toast.LENGTH_SHORT).show();

                                                if(ingredient.getQuantite()<=0){
                                                    listIngredients.remove(POS);
                                                }

                                                refresh();






                                        }else {
                                            Toast.makeText(getApplicationContext(), "La quantité ne peut être nulle", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                })
                        .setNegativeButton("Annuler",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



                //textViewValeurIngredients.setText(String.valueOf((double) val / 100)+"$");


                return true;
            }
        });





        textViewValeurIngredients = (TextView)findViewById(R.id.textViewValeur);
        float val = 0;
        for (Ingredient i:listIngredients) {
            System.out.println(i.getPrixTotal());
            val = val +i.getPrixTotal();
        }

        textViewValeurIngredients.setText(String.valueOf((double)val/100)+"$");




        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume(){
        super.onResume();
        refresh();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null){
            if(requestCode == 123){

                String jsonIngredient;

                Bundle extras = data.getExtras();
                if (extras != null) {
                    jsonIngredient = extras.getString("ingredient");
                    Toast.makeText(getApplicationContext(), jsonIngredient, Toast.LENGTH_SHORT).show();
                } else {
                    jsonIngredient = null;
                }
                Ingredient ingredientAjout = new Gson().fromJson(jsonIngredient, Ingredient.class);
                System.out.println("WOLOLO");

                dbm.addIngredientFrigo(ingredientAjout);


            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    public void refresh(){
        ArrayList<Ingredient> ingredients= dbm.getAllIngredientFrigo();

        listIngredients.removeAll(listIngredients);

        for (Ingredient i :ingredients) {
            listIngredients.add(i);


        }

        adapterListViewIngredients.notifyDataSetChanged();
        float val = 0;

        for (Ingredient in:listIngredients) {
            System.out.println(in.getPrixTotal());
            val = val +in.getPrixTotal();
        }

        textViewValeurIngredients.setText(String.valueOf((double) val / 100)+"$");

    }
}

