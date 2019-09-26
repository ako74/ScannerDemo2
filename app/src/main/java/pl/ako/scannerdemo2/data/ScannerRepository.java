package pl.ako.scannerdemo2.data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewOutlineProvider;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ScannerRepository {
    private final String TAG = ScannerRepository.class.getSimpleName();
    private ScannerDao mDao;
    private LiveData<List<ScannedLine>> mAllData;

    public ScannerRepository(Application application) {
        ScannerDatabase database = ScannerDatabase.getInstance(application);
        mDao = database.scannerDao();
        mAllData = mDao.selectAll();
        Log.d(TAG, "data fetched");
    }

    public LiveData<List<ScannedLine>> getAllData() {
        return mAllData;
    }

    public void insert(ScannedLine scannedLine) {
        new insertAsyncTask(mDao).execute(scannedLine);
    }

    public void deleteAll()  {
        new deleteAllAsyncTask(mDao).execute();
    }

    public void delete(long id) {
        new deleteAsyncTask(mDao).execute(id);
    }

    public void update(ScannedLine scannedLine) {
        new updateAsyncTask(mDao).execute(scannedLine);
    }

    private static class insertAsyncTask extends AsyncTask<ScannedLine, Void, Void> {

        private ScannerDao mAsyncTaskDao;

        public insertAsyncTask(ScannerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ScannedLine... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private ScannerDao mAsyncTaskDao;

        deleteAllAsyncTask(ScannerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Long, Void, Void> {
        private ScannerDao mAsyncTaskDao;

        deleteAsyncTask(ScannerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            mAsyncTaskDao.deleteById(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<ScannedLine, Void, Void> {

        private ScannerDao mDao;

        public updateAsyncTask(ScannerDao mDao) {
            this.mDao = mDao;
        }

        @Override
        protected Void doInBackground(ScannedLine... scannedLines) {
            mDao.update(scannedLines[0]);
            return null;
        }
    }

}
