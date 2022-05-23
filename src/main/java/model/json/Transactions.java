package model.json;

import java.util.Objects;

//VARIABLES REMOVED ON THIS FILES
public class Transactions {

    private String _id;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transactions)) return false;
        Transactions that = (Transactions) o;
        return get_id().equals(that.get_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id());
    }



    public String get_id() {
        return _id;
    }


}
