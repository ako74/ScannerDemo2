package pl.ako.scannerdemo2;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Scanner;

import pl.ako.scannerdemo2.data.ScannedLine;
import pl.ako.scannerdemo2.data.ScannerDao;
import pl.ako.scannerdemo2.data.ScannerRepository;

public class ScannerViewModel extends AndroidViewModel {
    private ScannerRepository mRepository;

    private LiveData<List<ScannedLine>> mAllData;

    public LiveData<List<ScannedLine>> getAllData() {
        return mAllData;
    }

    public ScannerViewModel(Application application) {
        super(application);
        mRepository = new ScannerRepository(application);
        mAllData = mRepository.getAllData();
    }

    public void insert(ScannedLine scannedLine) {
        mRepository.insert(scannedLine);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteById(long id) {
        mRepository.delete(id);
    }

    public void update(ScannedLine scannedLine) {
        mRepository.update(scannedLine);
    }
}
