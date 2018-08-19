package me.rytek.liveshoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    //listview
    private ListView mItemListView;
    private ArrayAdapter<Item> mAdapter;
    private ArrayList<Item> itemList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    //http
    private String url = "https://rytek.me/shoppingList";
    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Gson g = new Gson();

    //app
    private String user = "Ryan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this::handleActionButtonClick);

        mItemListView = (ListView) findViewById(R.id.shopping_list);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        mAdapter = new ItemAdaptor(this, R.layout.item_shopping_list, itemList);

//        mAdapter = new ArrayAdapter<>(this,
//                R.layout.item_shopping_list,
//                R.id.item_title,
//                itemList);
        mItemListView.setAdapter(mAdapter);


        mItemListView.setOnItemClickListener(this::handleBought);

        mItemListView.setOnItemLongClickListener(this::handleDelete);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
            refreshItems();
        });

        //set user
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        user = sharedPrefs.getString("prefUsername", "Ryan");

        //get items
        getItems();
    }

    private void refreshItems() {
        Handler handler = new Handler();
        Request request = new Request.Builder()
                .url(url + "/")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                e.printStackTrace();
                Snackbar.make(findViewById(R.id.shopping_list), "Failed to get items", Snackbar.LENGTH_SHORT).show();
                handler.post(() -> swipeRefreshLayout.setRefreshing(false));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d(TAG, json);
                itemList.clear();
                Item[] items = g.fromJson(json, Item[].class);
                itemList.addAll(Arrays.asList(items));
                handler.post(() -> {
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                });

            }
        });
    }

    private boolean handleDelete(AdapterView<?> parent, View view, int position, long id) {
        Item item = (Item) mItemListView.getItemAtPosition(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this item? " + item.item)
                .setPositiveButton("Yes", (dialog, id1) -> {
                    // FIRE ZE MISSILES!
                    removeItem(item);
                })
                .setNegativeButton("Cancel", (dialog, id12) -> {
                    // User cancelled the dialog
                });
        builder.create().show();

        return true;
    }

    private void addItem(Item item) {
        RequestBody body = RequestBody.create(JSON, g.toJson(item));
        Request request = new Request.Builder()
                .url(url + "/add")
                .post(body)
                .build();

        Handler handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                e.printStackTrace();

                Snackbar.make(findViewById(R.id.shopping_list), "Failed to delete. Try again later", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) {
                itemList.add(item);
                handler.post(() -> mAdapter.notifyDataSetChanged());
            }
        });
    }


    private void removeItem(Item item) {
        Request request = new Request.Builder()
                .url(url + "/" + item.id)
                .delete()
                .build();

        Handler handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                e.printStackTrace();

                Snackbar.make(findViewById(R.id.shopping_list), "Failed to delete. Try again later", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) {
                itemList.remove(item);
                handler.post(() -> mAdapter.notifyDataSetChanged());
            }
        });
    }

    private void handleBought(AdapterView<?> parent, View view, int position, long id) {
        Item item = (Item) mItemListView.getItemAtPosition(position);
        item.bought = !item.bought;

        Handler handler = new Handler();

        RequestBody body = RequestBody.create(JSON, g.toJson(item));
        Request request = new Request.Builder()
                .url(url + "/bought")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                e.printStackTrace();

                Snackbar.make(findViewById(R.id.shopping_list), "Failed to set as bought", Snackbar.LENGTH_SHORT).show();
                item.bought = !item.bought;
            }

            @Override
            public void onResponse(Call call, Response response) {
                handler.post(() -> toggleBought(item, view));
            }
        });
    }

    private void toggleBought(Item item, View view) {
        if (item.bought) {
            Snackbar.make(view, "Marking off item: " + item.item, Snackbar.LENGTH_LONG).show();
        }
        else {
            Snackbar.make(view, "Unmarking off item: " + item.item, Snackbar.LENGTH_LONG).show();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, UserSettingActivity.class));
            return true;
        }
        else if (id == R.id.action_refresh) {
            swipeRefreshLayout.setRefreshing(true);
            refreshItems();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleActionButtonClick(View view) {
        Log.d(TAG, "Add a new task");

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_item, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add an item to Buy")
                .setView(mView)
                .setPositiveButton("Add", (dialog1, which) -> {
                    String name = ((EditText) mView.findViewById(R.id.editText_item_name)).getText().toString();
                    String quantity = ((EditText) mView.findViewById(R.id.editText_quantity)).getText().toString();
                    String comments = ((EditText) mView.findViewById(R.id.editText_comments)).getText().toString();
                    if (quantity.equals(""))
                        quantity = "1";
                    Log.d(TAG, "Task to add: " + name);
                    addItem(new Item(name, Integer.parseInt(quantity), false, user, comments));
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }


    private void getItems() {
        Request request = new Request.Builder()
                .url(url + "/")
                .build();

        Handler handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();

                e.printStackTrace();
                Snackbar.make(findViewById(R.id.shopping_list), "Failed to get items", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d(TAG, json);

                Item[] items = g.fromJson(json, Item[].class);
                itemList.addAll(Arrays.asList(items));
                handler.post(() -> mAdapter.notifyDataSetChanged());
            }
        });
    }
}
