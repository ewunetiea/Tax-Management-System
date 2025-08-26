package com.afr.fms.Model.MGT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDocumentationChild {
    private Long id;
    private String accountNumber;
    private String loanType;
    private Double loanAmount;
    private Double outstanding_balane;
    private Double outstandingBalance;
    private String loanRepaymentAndInstallment;
    private String category;
    private Long credit_documentation_parent_id;
    private Double repayment_amount;
    private Double fees_and_charges;
    private Double fee_charge_collected;
    private Double fee_charge_uncollected;
    private String fee_commision_type;
    private String fee_commision_category;
    private String finding;
    private String finding_detail;
    private String impact;
    private String recommendation;
}
