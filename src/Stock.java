public class Stock {
    private String no;
    private String code;
    private String company;
    private String totalListedStock;
    private String status;

    public Stock(String no, String code, String company, String totalListedStock, String status) {
        this.no = no;
        this.code = code;
        this.company = company;
        this.totalListedStock = totalListedStock;
        this.status = status;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTotalListedStock() {
        return totalListedStock;
    }

    public void setTotalListedStock(String totalListedStock) {
        this.totalListedStock = totalListedStock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "no='" + no + '\'' +
                ", code='" + code + '\'' +
                ", company='" + company + '\'' +
                ", totalListedStock='" + totalListedStock + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
