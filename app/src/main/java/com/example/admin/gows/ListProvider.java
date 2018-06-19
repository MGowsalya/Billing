package com.example.admin.gows;

/**
 * Created by ADMIN on 12/13/2017.
 */

public class ListProvider {
    private String sno,qty,product;
    private Float rate,amt;


    public ListProvider(String sno, String product, Float rate, String qty, Float amt ){
        this.setSno(sno);
        this.setProduct(product);
        this.setRate(rate);
        this.setQty(qty);
        this.setAmt(amt);

    }
    public String getSno(){
        return sno;
    }
    public void setSno(String sno){
         this.sno = sno;
    }
    public Float getRate(){
        return rate;
    }
    public void setRate(Float rate)
    {
         this.rate = rate;
    }
    public Float getAmt(){
        return amt;
    }
    public void setAmt(Float amt){
         this.amt = amt;
    }
    public String getQty(){
        return qty;
    }
    public void setQty(String qty){
         this.qty = qty;
    }
    public String getProduct(){
        return product;
    }
    public void setProduct(String product){
         this.product = product;
    }
}
