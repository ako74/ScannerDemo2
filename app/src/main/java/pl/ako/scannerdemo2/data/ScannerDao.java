package pl.ako.scannerdemo2.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScannerDao {

    @Query("SELECT * FROM " + ScannedLine.TABLE_NAME)
    int count();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(ScannedLine line);

    @Query("SELECT * FROM " + ScannedLine.TABLE_NAME)
    LiveData<List<ScannedLine>> selectAll();

//    @Query("SELECT * FROM " + ScannedLine.TABLE_NAME + " WHERE " + ScannedLine.COLUMN_ID + " = :id")
//    LiveData<ScannedLine> selectById(long id);

    @Query("DELETE FROM " + ScannedLine.TABLE_NAME + " WHERE " + ScannedLine.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Query("DELETE FROM " + ScannedLine.TABLE_NAME)
    void deleteAll();

    @Update
    int update(ScannedLine line);

}
