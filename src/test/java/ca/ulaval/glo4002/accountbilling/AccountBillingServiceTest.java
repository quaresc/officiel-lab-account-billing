package ca.ulaval.glo4002.accountbilling;

import org.junit.Test;

import java.util.List;

public class AccountBillingServiceTest {
    private List<Bill> billsToRedistribute;

    private Bill billToCancel;

    // TODO : caracterisation tests.

    @Test(expected = BillNotFoundException.class)
    public void givenNoBill_thenThrows() {
        TestableAccountBillingService service = new TestableAccountBillingService();
        billToCancel = null;
        service.cancelInvoiceAndRedistributeFunds(new BillId(0));
    }

    class TestableAccountBillingService extends AccountBillingService {
        @Override
        protected List<Bill> findBillsToRedistribute(ClientId billClientId) {
            return billsToRedistribute;
        }

        @Override
        protected Bill findBill(BillId billId) {
            return billToCancel;
        }

        @Override
        protected void saveBill(Bill bill) {
            
        }
    }
}
