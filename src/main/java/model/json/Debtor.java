package model.json;

import java.util.Objects;

public class Debtor {
    private String _id;
    private String companyName;
    private String companyRuc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Debtor)) return false;
        Debtor debtor = (Debtor) o;
        return get_id().equals(debtor.get_id());
    }

    public String getCompanyRuc() {
        return companyRuc;
    }

    public void setCompanyRuc(String companyRuc) {
        this.companyRuc = companyRuc;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
