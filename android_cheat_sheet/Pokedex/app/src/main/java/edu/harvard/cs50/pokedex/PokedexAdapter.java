package edu.harvard.cs50.pokedex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> implements Filterable {
    private ArrayList<Pokemon> filtered=new ArrayList<>();
    private List<Pokemon> pokemon = new ArrayList<>();

    public static Set<String> set=new HashSet<>();



    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView textView;

        PokedexViewHolder(View view) {
            super(view);

            containerView = view.findViewById(R.id.pokedex_row);
            textView = view.findViewById(R.id.pokedex_row_text_view);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pokemon current = (Pokemon) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), PokemonActivity.class);
                    intent.putExtra("url", current.getUrl());
                    if(set.contains(current.getName()))
                    {intent.putExtra("catalog",true);}
                    else {intent.putExtra("catalog",false);}


                    v.getContext().startActivity(intent);
                }
            });
        }
    }


    private RequestQueue requestQueue;

    PokedexAdapter(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        loadPokemon();
    }

    public void loadPokemon() {
        String url = "https://pokeapi.co/api/v2/pokemon?limit=151";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);

                        String name = result.getString("name");
                        filtered.add(new Pokemon(
                                name.substring(0, 1).toUpperCase() + name.substring(1),
                                result.getString("url")
                        ));
                        pokemon.add(new Pokemon(
                            name.substring(0, 1).toUpperCase() + name.substring(1),
                            result.getString("url")
                        ));
                    }

                    notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("cs50", "Json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon list error", error);
            }
        });

        requestQueue.add(request);
    }

    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokedex_row, parent, false);

        return new PokedexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        Pokemon current = filtered.get(position);
        holder.textView.setText(current.getName());
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }
    @Override
    public Filter getFilter() {
        return new PokemonFilter();
    }
    public class PokemonFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if(charSequence==null ||charSequence.length()==0){
                results.values=pokemon;
                results.count=pokemon.size();
                return results;
            }
            else{
                ArrayList<Pokemon> filteredPokemon=new ArrayList<>();
                for(Pokemon this_pokemon: pokemon){
                    if((this_pokemon.getName()).toLowerCase().contains((String.valueOf(charSequence)).toLowerCase())){
                        filteredPokemon.add(this_pokemon);
                    }
                }
                results.values = filteredPokemon; // you need to create this variable!
                results.count = filteredPokemon.size();
                return results;
            }

            //implementing search



        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered= (ArrayList<Pokemon>) filterResults.values;
            notifyDataSetChanged();

        }
    }
    public static void addpokemon(String name){
        set.add(name);
    }
    public static void rmpokemon(String name){
        set.remove(name);
    }
    public static Set<String> getSet(){
        return set;
    }
    public  static void setSet(Set<String> temp){
        if(temp!=null){
         set=temp;}
    }



}
