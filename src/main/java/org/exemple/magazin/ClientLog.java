package org.exemple.magazin;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    protected List<String[]> shopLog = new ArrayList<>();
    protected int indexOfLine = 1;

    public ClientLog() {
        shopLog.add(new String[]{"1", "productNum", "amount"});
    }

    public void log(int productNum, int amount) {
        shopLog.add(new String[]{String.valueOf(indexOfLine), String.valueOf(productNum), String.valueOf(amount)});
        indexOfLine++;
    }

    public void exportAsCSV(File txtFile) throws IOException {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(txtFile))) {
            csvWriter.writeAll(shopLog);
        }
    }
}
