package com.financeautomationmini.controller;

import com.financeautomationmini.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
public class TransactionController {

        @Autowired
        private TransactionService service;

        @PostMapping("/upload")
        public String upload(@RequestParam("file") MultipartFile file) throws Exception {
            service.processFile(file);
            return "File processed successfully";
        }

        @GetMapping("/summary")
        public Map<String, Double> summary() {
            return service.getSummary();
        }

//==============getSummaryWithAllData===========//

    @GetMapping("/summarywithallData")
    public Map<String, Object> getSummaryWithAllData() {
        return service.getSummaryWithAllData();
    }

}
