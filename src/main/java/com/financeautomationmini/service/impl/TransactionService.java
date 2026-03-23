package com.financeautomationmini.service.impl;

import com.financeautomationmini.entity.TransactionData;
import com.financeautomationmini.repo.TransactionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    // ================= MAIN METHOD =================
    public void processFile(MultipartFile file) throws Exception {

        String fileName = file.getOriginalFilename();

        if (fileName != null && fileName.endsWith(".xlsx")) {
            processExcel(file);
        } else {
            processCSV(file);
        }
    }

// ================= CSV PROCESS =====================//
    private void processCSV(MultipartFile file) throws Exception {

        List<TransactionData> list = new ArrayList<>();
        Set<String> uniqueSet = new HashSet<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;

        boolean isHeader = true;

        while ((line = reader.readLine()) != null) {

            if (isHeader) {
                isHeader = false;
                continue;
            }

            String[] data = line.split(",");

            if (data.length < 5) continue;

            // Skip missing data
            if (data[1] == null || data[1].isEmpty()) continue;

            LocalDate date = LocalDate.parse(data[0]);
            String party = data[1];
            double amount = Double.parseDouble(data[2]);

            // find Duplicate check
            String key = date + "-" + party + "-" + amount;
            if (uniqueSet.contains(key)) continue;
            uniqueSet.add(key);

            TransactionData t = new TransactionData();

            t.setDate(date);
            t.setPartyName(party);
            t.setAmount(amount);
            t.setGstPercent(Double.parseDouble(data[3]));
            t.setInvoiceType(data[4]);

            processCommonFields(t);

            list.add(t);
        }

        repository.saveAll(list);
    }

    // ================= EXCEL PROCESS ====================//
    private void processExcel(MultipartFile file) throws Exception {

        List<TransactionData> list = new ArrayList<>();
        Set<String> uniqueSet = new HashSet<>();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        DataFormatter formatter = new DataFormatter();

        for (Row row : sheet) {

            if (row.getRowNum() == 0) continue; // skip header

            try {

                String dateStr = formatter.formatCellValue(row.getCell(0));
                String party = formatter.formatCellValue(row.getCell(1));
                String amountStr = formatter.formatCellValue(row.getCell(2));
                String gstStr = formatter.formatCellValue(row.getCell(3));
                String type = formatter.formatCellValue(row.getCell(4));

                //  skip invalid rows
                if (party == null || party.isEmpty()) continue;

                LocalDate date = parseDate(dateStr);
                double amount = Double.parseDouble(amountStr);
                double gstPercent = Double.parseDouble(gstStr);

                // duplicate check
                String key = date + "-" + party + "-" + amount;
                if (uniqueSet.contains(key)) continue;
                uniqueSet.add(key);

                TransactionData t = new TransactionData();
                t.setDate(date);
                t.setPartyName(party);
                t.setAmount(amount);
                t.setGstPercent(gstPercent);
                t.setInvoiceType(type);

                processCommonFields(t);

                list.add(t);

            } catch (Exception e) {
                // skip bad row instead of failing whole file
                System.out.println("Skipping row: " + row.getRowNum());
            }
        }

        workbook.close();
        repository.saveAll(list);
    }

    private LocalDate parseDate(String dateStr) {

        try {
            return LocalDate.parse(dateStr); // yyyy-MM-dd
        } catch (Exception e1) {
            try {
                return LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (Exception e2) {
                return LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            }
        }
    }

    // ================= COMMON LOGIC =================
    private void processCommonFields(TransactionData t) {

        double gstAmount = t.getAmount() * t.getGstPercent() / 100;
        t.setGstAmount(gstAmount);

        t.setHighValue(t.getAmount() > 50000);
        t.setMonth(t.getDate().getMonth().toString());
    }

// ================= GET SUMMARY ======================//
    public Map<String, Double> getSummary() {

        List<TransactionData> all = repository.findAll();

        double sales = 0;
        double purchase = 0;
        double gst = 0;

        for (TransactionData t : all) {

            if ("SALES".equalsIgnoreCase(t.getInvoiceType())) {
                sales += t.getAmount();
            } else {
                purchase += t.getAmount();
            }

            gst += t.getGstAmount();
        }

        Map<String, Double> map = new HashMap<>();
        map.put("totalSales", sales);
        map.put("totalPurchase", purchase);
        map.put("totalGST", gst);

        return map;
    }


//=============getSummaryWithAllData================//
    public Map<String, Object> getSummaryWithAllData() {

        List<TransactionData> all = repository.findAll();

        double sales = 0;
        double purchase = 0;
        double gst = 0;

        for (TransactionData t : all) {

            if ("SALES".equalsIgnoreCase(t.getInvoiceType())) {
                sales += t.getAmount();
            } else {
                purchase += t.getAmount();
            }

            gst += t.getGstAmount();
        }

        Map<String, Object> map = new HashMap<>();
       // Add All data with  list
        map.put("data", all);

        map.put("totalSales", sales);
        map.put("totalPurchase", purchase);
        map.put("totalGST", gst);

        map.put("message","Data Fetch Successfully !!!");
        map.put("status","200");

        return map;
    }
}