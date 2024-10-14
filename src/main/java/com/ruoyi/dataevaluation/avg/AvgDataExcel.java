package com.ruoyi.dataevaluation.avg;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;


public class AvgDataExcel {
    //员工姓名
    @Excel(name = "被评价人")
    String employeeName;
    //被评价人单位
    @Excel(name = "被评价人单位")
    String employeeCompany;
    //沟通协调
    @Excel(name = "沟通协调平均分")
    Double Communication;
    //敬业精神
    @Excel(name = "敬业精神平均分")
    Double Dedicated;
    //全局思维
    @Excel(name = "全局思维平均分")
    Double thinking;
    //团队管理
    @Excel(name = "团队管理平均分")
    Double management;
    //问题解决
    @Excel(name = "问题解决平均分")
    Double solve;
    //学习能力
    @Excel(name = "学习能力平均分")
    Double study;
    //应变处置
    @Excel(name = "应变处置平均分")
    Double dispose;
    //责任意识
    @Excel(name = "责任意识平均分")
    Double duty;

    public AvgDataExcel(String employeeName, String employeeCompany, Double communication, Double dedicated, Double thinking, Double management, Double solve, Double study, Double dispose, Double duty) {
        this.employeeName = employeeName;
        this.employeeCompany = employeeCompany;
        Communication = communication;
        Dedicated = dedicated;
        this.thinking = thinking;
        this.management = management;
        this.solve = solve;
        this.study = study;
        this.dispose = dispose;
        this.duty = duty;
    }

    public AvgDataExcel() {
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCompany() {
        return employeeCompany;
    }

    public void setEmployeeCompany(String employeeCompany) {
        this.employeeCompany = employeeCompany;
    }

    public Double getCommunication() {
        return Communication;
    }

    public void setCommunication(Double communication) {
        Communication = communication;
    }

    public Double getDedicated() {
        return Dedicated;
    }

    public void setDedicated(Double dedicated) {
        Dedicated = dedicated;
    }

    public Double getThinking() {
        return thinking;
    }

    public void setThinking(Double thinking) {
        this.thinking = thinking;
    }

    public Double getManagement() {
        return management;
    }

    public void setManagement(Double management) {
        this.management = management;
    }

    public Double getSolve() {
        return solve;
    }

    public void setSolve(Double solve) {
        this.solve = solve;
    }

    public Double getStudy() {
        return study;
    }

    public void setStudy(Double study) {
        this.study = study;
    }

    public Double getDispose() {
        return dispose;
    }

    public void setDispose(Double dispose) {
        this.dispose = dispose;
    }

    public Double getDuty() {
        return duty;
    }

    public void setDuty(Double duty) {
        this.duty = duty;
    }
}
