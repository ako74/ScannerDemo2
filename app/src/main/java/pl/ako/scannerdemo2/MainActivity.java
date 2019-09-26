package pl.ako.scannerdemo2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import pl.ako.scannerdemo2.data.ScannedLine;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int NEW_BARCODE_REQUEST = 1;
    public static final int EDIT_BARCODE_REQUEST = 2;

    private ScannerViewModel mScannerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewScanActivity.class);
                startActivityForResult(intent, NEW_BARCODE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScannerLinesAdapter adapter = new ScannerLinesAdapter(this, new ScannerLinesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScannedLine item) {
                Intent intent = new Intent(MainActivity.this, NewScanActivity.class);
                intent.putExtra(ExtraKeys.EXTRA_BARCODE, item.barcode);
                intent.putExtra(ExtraKeys.EXTRA_ID, item.id);
                intent.putExtra(ExtraKeys.EXTRA_QTY, item.quantity.toString());
                startActivityForResult(intent, EDIT_BARCODE_REQUEST);
            }
        });


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mScannerViewModel = ViewModelProviders.of(this).get(ScannerViewModel.class);

        mScannerViewModel.getAllData().observe(this, new Observer<List<ScannedLine>>() {
            @Override
            public void onChanged(@Nullable final List<ScannedLine> data) {
                // Update the cached copy of the words in the adapter.
                adapter.setData(data);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        ScannedLine scannedLine = adapter.getScannedLineAtPosition(position);
                        Toast.makeText(MainActivity.this, "Deleting barcode: " + scannedLine.barcode, Toast.LENGTH_LONG).show();
                        mScannerViewModel.deleteById(scannedLine.id);
                    }
                }
        );

        helper.attachToRecyclerView(recyclerView);
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
            return true;
        }

        if (id == R.id.clear_data) {
            // Add a toast just for confirmation
            Toast.makeText(this, "Clearing the data...",
                    Toast.LENGTH_SHORT).show();

            // Delete the existing data
            mScannerViewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BARCODE_REQUEST && resultCode == RESULT_OK) {
            ScannedLine item = new ScannedLine(
                    0l,
                    data.getStringExtra(ExtraKeys.EXTRA_BARCODE),
                    Integer.parseInt(data.getStringExtra(ExtraKeys.EXTRA_QTY))
            );
            mScannerViewModel.insert(item);
        } else if (requestCode == EDIT_BARCODE_REQUEST && resultCode == RESULT_OK) {
            ScannedLine item = new ScannedLine(
                    data.getLongExtra(ExtraKeys.EXTRA_ID,0),
                    data.getStringExtra(ExtraKeys.EXTRA_BARCODE),
                    Integer.parseInt(data.getStringExtra(ExtraKeys.EXTRA_QTY))
            );
            mScannerViewModel.update(item);
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

//    protected void setFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }


}
