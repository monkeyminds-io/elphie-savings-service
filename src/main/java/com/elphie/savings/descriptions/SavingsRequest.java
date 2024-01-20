package com.elphie.savings.descriptions;

@Data
public class SavingsRequest {
    
    // PROPERTIES ////////////////
    private String accountId;
    private String name;
    private String targetAmount;
    private String targetDate;

    // GETTERS & SETTERS ////////////////
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTargetAmount() { return targetAmount; }
    public void setTargetAmount(String targetAmount) { this.targetAmount = targetAmount; }
    public String getTargetDate() { return targetDate; }
    public void setTargetDate(String targetDate) { this.targetDate = targetDate; }
}
