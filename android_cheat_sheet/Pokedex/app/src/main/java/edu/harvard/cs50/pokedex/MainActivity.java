package edu.harvard.cs50.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.Set;
//import android.widget.SearchView;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    Set<String> bypassing=new HashSet<>();
   // private RecyclerView.Adapter adapter;
    private PokedexAdapter adapter; //just a pokedexadapter object
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new PokedexAdapter(getApplicationContext());
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem search_item=menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;


    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        adapter.getFilter().filter(s);
        return false;

    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences current=getSharedPreferences("pokedex",MODE_PRIVATE);
        bypassing=current.getStringSet("Pokemon",null);

        PokedexAdapter.setSet(bypassing);



    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences current=getSharedPreferences("pokedex",MODE_PRIVATE);
        SharedPreferences.Editor teamrocket=current.edit();
        bypassing=PokedexAdapter.getSet();
        teamrocket.putStringSet("Pokemon",bypassing);
        teamrocket.apply();
        teamrocket.commit();

    }
}
