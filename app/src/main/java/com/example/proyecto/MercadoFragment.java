package com.example.proyecto;

// ... (imports) ...

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MercadoFragment extends Fragment {

    // ... (Existing code: newInstance, onCreate, etc.) ...

    private TableLayout tableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mercado, container, false);
        tableLayout = view.findViewById(R.id.tableLayout); // Assuming you have this ID in your layout
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addRowsToTable();
    }

    private void addRowsToTable() {
        List<List<String>> data = getTableData();

        for (List<String> rowData : data) {
            TableRow tableRow = new TableRow(requireContext());

            for (String cellData : rowData) {
                TextView textView = new TextView(requireContext());
                textView.setText(cellData);
                textView.setPadding(16, 16, 16, 16);
                tableRow.addView(textView);
            }

            tableLayout.addView(tableRow);
        }
    }

    private List<List<String>> getTableData() {
        // Replace with your logic to fetch table data
        List<List<String>> data = new ArrayList<>();
        data.add(Arrays.asList("Header 1", "Header 2", "Header 3"));
        data.add(Arrays.asList("Data 1", "Data 2", "Data 3"));
        data.add(Arrays.asList("Data 4", "Data 5", "Data 6"));
        // ... more rows
        return data;
    }
}