package com.ruoyi.dataevaluation.main;


import com.ruoyi.framework.aspectj.lang.annotation.Excel;

/**
 * 评分列表对象
 */
public class Rating {
    //评委
    @Excel(name = "评分人")
    String evaluatorName;
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
    @Excel(name = "姓名")
    String employeeName;
    //员工编号
    @Excel(name = "员工编号")
    String employeeCode;
    //岗位类别
    @Excel(name = "岗位类别")
    String employeePostType;
    //指标
    @Excel(name = "指标")
    String indicator;
    //分数
    @Excel(name = "评价分")
    Double score;

    public Rating(String employeeCode, String employeeName, String employeeCompany, String employeeDept, String employeePost, String employeePostType, String indicator, String evaluatorName, Double score) {
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.employeeCompany = employeeCompany;
        this.employeeDept = employeeDept;
        this.employeePost = employeePost;
        this.employeePostType = employeePostType;
        this.indicator = indicator;
        this.evaluatorName = evaluatorName;
        this.score = score;
    }

    public Rating() {
    }

    public Rating(String employeeName, String employeeCompany, String indicator, String evaluatorName, Double score) {
        this.employeeName = employeeName;
        this.employeeCompany = employeeCompany;
        this.indicator = indicator;
        this.evaluatorName = evaluatorName;
        this.score = score;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
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

    public String getEmployeePostType() {
        return employeePostType;
    }

    public void setEmployeePostType(String employeePostType) {
        this.employeePostType = employeePostType;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getEvaluatorName() {
        return evaluatorName;
    }

    public void setEvaluatorName(String evaluatorName) {
        this.evaluatorName = evaluatorName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
