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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}
