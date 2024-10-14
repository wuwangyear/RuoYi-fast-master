package com.ruoyi.dataevaluation.newavg;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;


public class NewAvgDataExcel {
    //被评价人单位
    @Excel(name = "被评价人单位")
    String employeeCompany;
    //被评价人部门
    @Excel(name = "被评价人部门")
    String employeeDept;
    //岗位名称
    @Excel(name = "岗位名称")
    String employeePost;
    //员工姓名
    @Excel(name = "被评价人")
    String employeeName;
    //人员编码
    @Excel(name = "人员编码")
    String employeeCode;
    //岗位类别
    @Excel(name = "岗位类别")
    String employeePostType;
    //政治素质平均分
    @Excel(name = "政治素质平均分")
    Double political;
    //专业知识平均分
    @Excel(name = "专业知识平均分")
    Double professional;
    //沟通协调
    @Excel(name = "沟通协调平均分")
    Double communication;
    //敬业精神
    @Excel(name = "敬业精神平均分")
    Double dedicated;
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

    public NewAvgDataExcel() {
    }

    public NewAvgDataExcel(String employeeCompany, String employeeDept, String employeePost, String employeeName, String employeeCode, String employeePostType, Double political, Double professional, Double communication, Double dedicated, Double thinking, Double management, Double solve, Double study, Double dispose, Double duty) {
        this.employeeCompany = employeeCompany;
        this.employeeDept = employeeDept;
        this.employeePost = employeePost;
        this.employeeName = employeeName;
        this.employeeCode = employeeCode;
        this.employeePostType = employeePostType;
        this.political = political;
        this.professional = professional;
        this.communication = communication;
        this.dedicated = dedicated;
        this.thinking = thinking;
        this.management = management;
        this.solve = solve;
        this.study = study;
        this.dispose = dispose;
        this.duty = duty;
    }

    public String getEmployeeCompany() {
        return employeeCompany;
    }

    public void setEmployeeCompany(String employeeCompany) {
        this.employeeCompany = employeeCompany;
    }

    public String getEmployeeDept() {
        return employeeDept;
    }

    public void setEmployeeDept(String employeeDept) {
        this.employeeDept = employeeDept;
    }

    public String getEmployeePost() {
        return employeePost;
    }

    public void setEmployeePost(String employeePost) {
        this.employeePost = employeePost;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeePostType() {
        return employeePostType;
    }

    public void setEmployeePostType(String employeePostType) {
        this.employeePostType = employeePostType;
    }

    public Double getPolitical() {
        return political;
    }

    public void setPolitical(Double political) {
        this.political = political;
    }

    public Double getProfessional() {
        return professional;
    }

    public void setProfessional(Double professional) {
        this.professional = professional;
    }

    public Double getCommunication() {
        return communication;
    }

    public void setCommunication(Double communication) {
        this.communication = communication;
    }

    public Double getDedicated() {
        return dedicated;
    }

    public void setDedicated(Double dedicated) {
        this.dedicated = dedicated;
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
