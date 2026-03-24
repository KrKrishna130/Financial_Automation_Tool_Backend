package com.financeautomationmini.entity;

import jakarta.persistence.Entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class TransactionData {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private LocalDate date;
        private String partyName;
        private Double amount;
        private Double gstPercent;
        private String invoiceType; // SALES / PURCHASE

        private Double gstAmount;
        private boolean highValue;
        private String month;

        public Double getGstAmount() {
                return gstAmount;
        }

        public void setGstAmount(Double gstAmount) {
                this.gstAmount = gstAmount;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public LocalDate getDate() {
                return date;
        }

        public void setDate(LocalDate date) {
                this.date = date;
        }

        public String getPartyName() {
                return partyName;
        }

        public void setPartyName(String partyName) {
                this.partyName = partyName;
        }

        public Double getAmount() {
                return amount;
        }

        public void setAmount(Double amount) {
                this.amount = amount;
        }

        public Double getGstPercent() {
                return gstPercent;
        }

        public void setGstPercent(Double gstPercent) {
                this.gstPercent = gstPercent;
        }

        public String getInvoiceType() {
                return invoiceType;
        }

        public void setInvoiceType(String invoiceType) {
                this.invoiceType = invoiceType;
        }

        public boolean isHighValue() {
                return highValue;
        }

        public void setHighValue(boolean highValue) {
                this.highValue = highValue;
        }

        public String getMonth() {
                return month;
        }

        public void setMonth(String month) {
                this.month = month;
        }

        public TransactionData() {
        }
}
