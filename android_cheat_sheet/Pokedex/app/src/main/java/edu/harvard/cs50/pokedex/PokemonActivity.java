package edu.harvard.cs50.pokedex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class PokemonActivity extends AppCompatActivity {
    ArrayList<String> catalog=new ArrayList<>();
    TextView pokemon_details;
    private TextView nameTextView;
    private TextView numberTextView;
    private TextView type1TextView;
    private TextView type2TextView;
    ImageView pokemon_pics;
    private String url;
    private RequestQueue requestQueue;
    Button pokemoncatch;
    Boolean present;
    String pic_url;
    String desc_url="https://pokeapi.co/api/v2/pokemon-species/";
    String keyWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        url = getIntent().getStringExtra("url");
        present=getIntent().getBooleanExtra("catalog",false);
        pokemon_details=(TextView) findViewById(R.id.poke_desc);
        nameTextView = findViewById(R.id.pokemon_name);
        pokemon_pics=(ImageView)findViewById(R.id.image_pokemon);
        numberTextView = findViewById(R.id.pokemon_number);
        type1TextView = findViewById(R.id.pokemon_type1);
        type2TextView = findViewById(R.id.pokemon_type2);
        pokemoncatch=(Button) findViewById(R.id.pokeball);
        if(present){
            pokemoncatch.setText("Release");
        }


        load();
    }

    public void load() {
        type1TextView.setText("");
        type2TextView.setText("");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    keyWord=response.getString("name");
                    nameTextView.setText(keyWord);
                    load_details(keyWord);
                    numberTextView.setText(String.format("#%03d", response.getInt("id")));
                        JSONObject pic_step_1=response.getJSONObject("sprites");
                        pic_url=pic_step_1.getString("front_default");
                         Log.v("api",pic_url);
                         Getting_picture loading_pic=new Getting_picture();
                         loading_pic.execute(pic_url);
                    JSONArray typeEntries = response.getJSONArray("types");
                    for (int i = 0; i < typeEntries.length(); i++) {
                        JSONObject typeEntry = typeEntries.getJSONObject(i);
                        int slot = typeEntry.getInt("slot");
                        String type = typeEntry.getJSONObject("type").getString("name");

                        if (slot == 1) {
                            type1TextView.setText(type);
                        }
                        else if (slot == 2) {
                            type2TextView.setText(type);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("cs50", "Pokemon json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon details error", error);
            }
        });

        requestQueue.add(request);

    }
    public void load_details(String name){
        Log.v("api_check",name);
        desc_url=desc_url+name+"/";
        Log.v("api_name",desc_url);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, desc_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray contain_detail = response.getJSONArray("flavor_text_entries");
                    JSONObject image_object=contain_detail.getJSONObject(1);
                    String full_desc = image_object.getString("flavor_text");
                    Log.v("api_url",full_desc);
                    pokemon_details.setText(full_desc);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("api","in exception");
                }
            }

            },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("api","api problem");
            }
        }
        );
        requestQueue.add(request);
    }



    public void toggleCatch(View view){
        String cur=String.valueOf(nameTextView.getText());
        if(present){
            PokedexAdapter.rmpokemon(cur.substring(0,1).toUpperCase()+cur.substring(1));
            pokemoncatch.setText("Catch");
            present=false;
        }else{
            PokedexAdapter.addpokemon(cur.substring(0,1).toUpperCase()+cur.substring(1));
            pokemoncatch.setText("Release");
            present=true;
        }

        load();

    }
    private  class Getting_picture extends AsyncTask<String,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{
                URL url=new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("image","loading error in image");
                return null;
            }

        }
        @Override
        protected  void onPostExecute(Bitmap bitmap){
            pokemon_pics.setImageBitmap(bitmap);

        }
    }





}
