package ca.ulaval.glo4002.accountbilling;

import java.util.List;

public class AccountBillingService {

    public void cancelInvoiceAndRedistributeFunds(BillId id) {
        Bill canceledBill = findBill(id);
        if (canceledBill == null) {
            throw new BillNotFoundException();
        } else {
            ClientId billClientId = canceledBill.getClientId();

            if (!canceledBill.isCancelled()) {
                canceledBill.cancel();
            }

            saveBill(canceledBill);

            List<Allocation> canceledAllocations = canceledBill.getAllocations();
            for (Allocation canceledAllocation : canceledAllocations) {
                List<Bill> billsToRedistribute = findBillsToRedistribute(billClientId);
                int canceledAllocationAmount = canceledAllocation.getAmount();

                for (Bill bill : billsToRedistribute) {
                    if (canceledBill != bill) {
                        int remainingAmount = bill.getRemainingAmount();
                        Allocation allocation;
                        if (remainingAmount <= canceledAllocationAmount) {
                            allocation = new Allocation(remainingAmount);
                            canceledAllocationAmount -= remainingAmount;
                        } else {
                            allocation = new Allocation(canceledAllocationAmount);
                            canceledAllocationAmount = 0;
                        }

                        bill.addAllocation(allocation);

                        saveBill(bill);
                    }
                    if (canceledAllocationAmount == 0) {
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
