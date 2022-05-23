package model.json;

import java.util.Date;
import java.util.Objects;

//VARIABLES REMOVED ON THIS FILE
public class InvoiceTransactions {

    private String _id;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceTransactions)) return false;
        InvoiceTransactions that = (InvoiceTransactions) o;
        return get_id().equals(that.get_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id());
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}
