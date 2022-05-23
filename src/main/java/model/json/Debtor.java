package model.json;

import java.util.Objects;

//VARIABLES REMOVED ON THIS FILE
public class Debtor {
    private String _id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Debtor)) return false;
        Debtor debtor = (Debtor) o;
        return get_id().equals(debtor.get_id());
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}
