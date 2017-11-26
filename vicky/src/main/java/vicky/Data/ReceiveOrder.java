package vicky.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ReceiveOrder {



    private Long id;
    private Boolean status;
    private String detail;
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updatetime;
    private Date createDay;
    private Integer num;
    private BigDecimal mat;
private ReceiveOrder child;
    private List<People> childs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

     public Double abcd;

    public Double getAbcd()
    {
        return abcd;
    }
    public void setAbcd(Double abcd)
    {
        this.abcd=abcd;
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



    private Timestamp updatetime1;

    public Timestamp getUpdatetime1() {
        return updatetime1;
    }

    public void setUpdatetime1(Timestamp updatetime1) {
        this.updatetime1 = updatetime1;
    }

    public ReceiveOrder getChild() {
        return child;
    }

    public void setChild(ReceiveOrder child) {
        this.child = child;
    }


    public List<People> getChilds() {
        return childs;
    }

    public void setChilds(List<People> childs) {
        this.childs = childs;
    }



}
