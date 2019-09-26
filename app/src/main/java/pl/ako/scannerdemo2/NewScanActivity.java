package pl.ako.scannerdemo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewScanActivity extends AppCompatActivity {
    private static final long NOT_SET_ID = 0;
    private EditText mEditBarcodeView;
    private EditText mEditQuantityView;
    private long mId;

    private Class<?> mClss;

    public static final int PICK_BARCODE_REQUEST = 1;
    private static final int ZXING_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_scan);
        mEditBarcodeView = findViewById(R.id.edit_barcode);
        mEditQuantityView = findViewById(R.id.edit_quantity);

        Intent intent = getIntent();
        mId = intent.getLongExtra(ExtraKeys.EXTRA_ID, NOT_SET_ID);
        mEditBarcodeView.setText(intent.getStringExtra(ExtraKeys.EXTRA_BARCODE));
        mEditQuantityView.setText(intent.getStringExtra(ExtraKeys.EXTRA_QTY));


        final Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditBarcodeView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String barcode = mEditBarcodeView.getText().toString();
                    String quantity = mEditQuantityView.getText().toString();
                    replyIntent.putExtra(ExtraKeys.EXTRA_BARCODE, barcode);
                    replyIntent.putExtra(ExtraKeys.EXTRA_ID, mId);
                    replyIntent.putExtra(ExtraKeys.EXTRA_QTY, quantity);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

        final Button buttonScan = findViewById(R.id.button_scan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchScanner();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    public void launchScanner() {
        Class<?> clss = ScannerActivity.class;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivityForResult(intent, PICK_BARCODE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_BARCODE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String barcodeText = bundle.getString(ExtraKeys.EXTRA_BARCODE);
                if (!barcodeText.isEmpty()) {
                    mEditBarcodeView.setText(barcodeText);
                    mEditQuantityView.setText("1");
                }
            }
        }
    }
}
