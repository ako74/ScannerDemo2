package pl.ako.scannerdemo2.data;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.text.style.TtsSpan;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = ScannedLine.TABLE_NAME)
public class ScannedLine {

    public static final String TABLE_NAME = "scanned_lines";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_QUANTITY = "qty";
    public static final String COLUMN_CREATED_AT = "created_at";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public Long id;

    @ColumnInfo(name = COLUMN_BARCODE)
    @NonNull
    public String barcode;

    @ColumnInfo(name = COLUMN_QUANTITY)
    @NonNull
    public Integer quantity;

    //    @ColumnInfo(name = COLUMN_CREATED_AT)
//    public Date createdAt;
    @Ignore
    public ScannedLine(Long id, @NonNull String barcode, @NonNull int quantity) {
        this.id = id;
        this.barcode = barcode;
        this.quantity = quantity;
    }

    public ScannedLine(@NonNull String barcode) {
        this.barcode = barcode;
    }

}
