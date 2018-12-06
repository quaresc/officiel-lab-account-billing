package ca.ulaval.glo4002.accountbilling;

import java.util.List;

public class BillDAO {

    public static final Object LOCK = new Object();

    public static BillDAO instance;

    public static BillDAO getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new BillDAO();
                }
            }
        }

        return instance;
    }

/*  Could do that
    public static void forceInstanceForTests(BillDAO dao) {
        instance = dao;
    }*/

    private BillDAO() {
    }

    public Bill findBill(BillId id) {
        throw new UnsupportedOperationException("For the purposes of this workshop, you can't call this.");
    }

    public List<Bill> findAllByClient(ClientId cid) {
        throw new UnsupportedOperationException("For the purposes of this workshop, you can't call this.");
    }

    public void persist(Bill b) {
        throw new UnsupportedOperationException("For the purposes of this workshop, you can't call this.");
    }

}
