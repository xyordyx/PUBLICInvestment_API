package model.json;

import java.util.List;

public class Invoice {
    private Debtor debtor;
    private List<PhysicalInvoices> physicalInvoices;
    private String _id;

    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    public List<PhysicalInvoices> getPhysicalInvoices() {
        return physicalInvoices;
    }

    public void setPhysicalInvoices(List<PhysicalInvoices> physicalInvoices) {
        this.physicalInvoices = physicalInvoices;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
