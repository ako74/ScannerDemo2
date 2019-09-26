package pl.ako.scannerdemo2.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ScannedLine.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class ScannerDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "scanner";
    private static ScannerDatabase sInstance;

    public abstract ScannerDao scannerDao();

    public static synchronized ScannerDatabase getInstance(Context context) {
        if(sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), ScannerDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(sScannerDatabaseCallback)
                    .build();
        }
        return sInstance;
    }

    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                ScannerDatabase.class)
                .addCallback(sScannerDatabaseCallback)
                .build();
    }


    private static RoomDatabase.Callback sScannerDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(sInstance).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private static String TAG = PopulateDbAsync.class.getSimpleName();

        private final ScannerDao mDao;
        String[] barcodes = {"5012909001632", "5903407131023", "5023117386107", "4006874016105"};

        PopulateDbAsync(ScannerDatabase db) {
            mDao = db.scannerDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            //mDao.deleteAll();
            if(mDao.count()==0) {
                for (int i = 0; i <= barcodes.length - 1; i++) {
                    ScannedLine item = new ScannedLine(barcodes[i]);
                    item.quantity=1;
                    mDao.insert(item);
                    Log.d(TAG, "inserted: " + barcodes[i]);
                }
            }
            return null;
        }
    }

}
