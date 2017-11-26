package vicky.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private List<People> childs;
    private Order child;
    private Long id;
    private Boolean status;
    private String detail;
    private Timestamp updatetime;
    private Date createDay;
    private Integer num;

    private String shuoming;
      private BigDecimal mat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreateDay() {
        return createDay;
    }

    public void setCreateDay(Date createDay) {
        this.createDay = createDay;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getMat() {
        return mat;
    }

    public void setMat(BigDecimal mat) {
        this.mat = mat;
    }

    public String getShuoming() {
        return shuoming;
    }

    public void setShuoming(String shuoming) {
        this.shuoming = shuoming;
    }

    public Order getChild() {
        return child;
    }

    public void setChild(Order child) {
        this.child = child;
    }


    public List<People> getChilds() {
        return childs;
    }

    public void setChilds(List<People> childs) {
        this.childs = childs;
    }
}
