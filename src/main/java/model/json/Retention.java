package model.json;

public class Retention {
    private String _id;
    private String amount;
    private Double retentionPercentage;
    private String status;
    private String type;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public Double getRetentionPercentage() {
        return retentionPercentage;
    }

    public void setRetentionPercentage(Double retentionPercentage) {
        this.retentionPercentage = retentionPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
