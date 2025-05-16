package com.expense.service.service;

import com.expense.service.dto.ExpenseDto;
import com.expense.service.entities.Expense;
import com.expense.service.repository.ExpenseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExpenseService {

    private ExpenseRepository expenseRepository;

    private ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository=expenseRepository;
    }

     public boolean createExpense(ExpenseDto expenseDto){
        setCurrecny(expenseDto);
        try{
            expenseRepository.save(objectMapper.convertValue(expenseDto, Expense.class));
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
     }


     public boolean updateExpense(ExpenseDto expenseDto){
         Optional<Expense> ExpenseFound = expenseRepository.findByUserIdAndExternalId(expenseDto.getUserId(),expenseDto.getExternalId());
         if(ExpenseFound.isEmpty()) return false;

         Expense expense=ExpenseFound.get();
         expense.setCurrency(Strings.isNotBlank(expense.getCurrency()) ? expenseDto.getCurrency() :expense.getCurrency());
         expense.setMerchant(Strings.isNotBlank(expense.getMerchant()) ? expenseDto.getMerchant() : expense.getMerchant());
         expense.setAmount(expenseDto.getAmount());
         expenseRepository.save(expense);
         return true;
     }

     public List<ExpenseDto> getExpense(String userId){
        List<Expense> expensesOpt =expenseRepository.findByUserId(userId);
         return objectMapper.convertValue(expensesOpt, new TypeReference<List<ExpenseDto>>() {});
     }

     private void setCurrecny(ExpenseDto expenseDto){
        if(Objects.isNull(expenseDto.getCurrency())){
            expenseDto.setCurrency("inr");
        }
    }
}
