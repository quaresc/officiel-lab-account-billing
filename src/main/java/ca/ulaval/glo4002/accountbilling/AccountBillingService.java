package ca.ulaval.glo4002.accountbilling;

import java.util.List;

public class AccountBillingService {

    public void cancelInvoiceAndRedistributeFunds(BillId id) {
        Bill billToCancel = findBill(id);
        if (billToCancel == null) {
            throw new BillNotFoundException();
        } else {
            ClientId billClientId = billToCancel.getClientId();

            if (!billToCancel.isCancelled()) {
                billToCancel.cancel();
            }

            saveBill(billToCancel);

            List<Allocation> canceledAllocations = billToCancel.getAllocations();
            for (Allocation canceledAllocation : canceledAllocations) {
                List<Bill> bills = findBillsToRedistribute(billClientId);
                int amount = canceledAllocation.getAmount();

                for (Bill possibleBill : bills) {
                    if (billToCancel != possibleBill) {
                        int remainingAmount = possibleBill.getRemainingAmount();
                        Allocation allocation;
                        if (remainingAmount <= amount) {
                            allocation = new Allocation(remainingAmount);
                            amount -= remainingAmount;
                        } else {
                            allocation = new Allocation(amount);
                            amount = 0;
                        }

                        possibleBill.addAllocation(allocation);

                        saveBill(possibleBill);
                    }

                    if (amount == 0) {
                        break;
                    }
                }
            }
        }
    }

    protected List<Bill> findBillsToRedistribute(ClientId billClientId) {
        return BillDAO.getInstance().findAllByClient(billClientId);
    }

    protected void saveBill(Bill bill) {
        BillDAO.getInstance().persist(bill);
    }

    protected Bill findBill(BillId billId) {
        return BillDAO.getInstance().findBill(billId);
    }
}
