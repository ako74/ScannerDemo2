package pl.ako.scannerdemo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.ako.scannerdemo2.data.ScannedLine;

public class ScannerLinesAdapter extends RecyclerView.Adapter<ScannerLinesAdapter.ScannerLineViewHolder> {
    private final LayoutInflater mInflater;
    private List<ScannedLine> mData;
    private ScannerLinesAdapter.OnItemClickListener mListner;

    ScannerLinesAdapter(Context context, ScannerLinesAdapter.OnItemClickListener listner) {
        mInflater = LayoutInflater.from(context);
        mListner = listner;
    }

    @Override
    public ScannerLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.scanner_line_item, parent, false);
        return new ScannerLineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScannerLineViewHolder holder, int position) {
        if (mData != null) {
            ScannedLine current = mData.get(position);
            holder.bind(current, mListner);
        } else {
            // Covers the case of data not being ready yet.
            holder.scannedLineView.setText("empty");
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        else return 0;
    }

    public void setData(List<ScannedLine> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public ScannedLine getScannedLineAtPosition (int position) {
        return mData.get(position);
    }

    class ScannerLineViewHolder extends RecyclerView.ViewHolder {
        private final TextView scannedLineView;
        private final TextView quantityLineView;

        private ScannerLineViewHolder(View itemView) {
            super(itemView);
            scannedLineView = itemView.findViewById(R.id.text_barcode);
            quantityLineView = itemView.findViewById(R.id.text_quantity);
        }

        void bind(final ScannedLine line, final ScannerLinesAdapter.OnItemClickListener listner) {
            scannedLineView.setText(line.barcode);
            quantityLineView.setText(line.quantity.toString());
            scannedLineView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClick(line);
                }
            });
        }
    }


    interface OnItemClickListener {
        void onItemClick(ScannedLine item);
    }
}
