package ca.ulaval.glo4002.accountbilling;

import java.util.List;

public class AccountBillingService {

    public void cancelInvoiceAndRedistributeFunds(BillId id) {
        Bill bill = BillDAO.getInstance().findBill(id);
        if (!(bill == null)) {
            ClientId cid = bill.getClientId();

            if (bill.isCancelled() != true) bill.cancel();
            BillDAO.getInstance().persist(bill);

            List<Allocation> a = bill.getAllocations();

            for (Allocation al : a) {
                List<Bill> bills = BillDAO.getInstance().findAllByClient(cid);
                int amount = al.getAmount();

                for (Bill b : bills) {
                    if (bill != b) {
                        int remainingAmount = b.getRemainingAmount();
                        Allocation allocation;
                        if (remainingAmount <= amount) {
                            allocation = new Allocation(remainingAmount);
                            amount -= remainingAmount;
                        } else {
                            allocation = new Allocation(amount);
                            amount = 0;
                        }

                        b.addAllocation(allocation);

                        BillDAO.getInstance().persist(b);
                    }

                    if (amount == 0) {
                        break;
                    }
                }
            }
        } else {
            throw new BillNotFoundException();
        }
    }
}
