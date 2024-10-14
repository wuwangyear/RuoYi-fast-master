package com.ruoyi.dataevaluation.newavg;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.dataevaluation.main.Rating;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 计算清洗前原数据员工指标平均分
 */
public class NewAvgData {
    List<Rating> ratings;
    List<NewAvgDataExcel> avgDataExcels = new ArrayList<>();
    Map<String,String> emplyeeCompanyMap = new HashMap<>();//员工单位
    Map<String, Map<String, Double>> indicatorEmployeeAvgScores = new HashMap<>();//每个指标下员工的平均成绩
    Map<String, Double> evaluatorAvgScores = new HashMap<>();//评委（某一位）打分的平均成绩
    Map<String, Double> indicatorAvgScores = new HashMap<>();//所有评委在该项指标下的平均成绩
    Map<String, Map<String, Map<String, Double>>> employeeIndicatorEvaluatorAvgScores = new HashMap<>();//所有评委在该项指标下对员工的平均成绩

    NewAvgData(List<NewEvaluationExcel> newEvaluationExcels) {
        List<Rating> ratings = newEvaluationToRating(newEvaluationExcels);
        this.ratings = ratings;
        calculateAverages();
    }

    // 计算每个指标下员工的平均成绩、评委的平均成绩等
    private void calculateAverages() {
        // 初始化必要的映射
        Map<String, Map<String, List<Double>>> indicatorEmployeeScores = new HashMap<>();//指标下员工评分
        Map<String, List<Double>> evaluatorScores = new HashMap<>();//某一位评委评分
        Map<String, List<Double>> indicatorScores = new HashMap<>();//所有评委在某指标下评分
        Map<String, Map<String, Map<String, List<Double>>>> employeeIndicatorEvaluatorScores = new HashMap<>();//所有评委在该指标下对员工的评分
        for (Rating rating : ratings) {
            //指标下员工评分
            indicatorEmployeeScores.computeIfAbsent(rating.getIndicator(), k -> new HashMap<>())
                    .computeIfAbsent(rating.getEmployeeCode(), k -> new ArrayList<>()).add(rating.getScore());
            //某一位的所有评分
            evaluatorScores.computeIfAbsent(rating.getEvaluatorName(), k -> new ArrayList<>()).add(rating.getScore());
            //所有评委在某指标下评分
            indicatorScores.computeIfAbsent(rating.getIndicator(), k -> new ArrayList<>()).add(rating.getScore());
            //所有评委在该指标下对员工的评分
            employeeIndicatorEvaluatorScores.computeIfAbsent(rating.getEmployeeCode(), k -> new HashMap<>())
                    .computeIfAbsent(rating.getIndicator(), k -> new HashMap<>())
                    .computeIfAbsent(rating.getEvaluatorName(), k -> new ArrayList<>()).add(rating.getScore());
        }

        // 计算每个员工在每个指标下的平均分数（一个指标下某个员工的平均分数）
        for (Map.Entry<String, Map<String, List<Double>>> entry : indicatorEmployeeScores.entrySet()) {
            Map<String, Double> avgMap = new HashMap<>();
            for (Map.Entry<String, List<Double>> employeeEntry : entry.getValue().entrySet()) {
                double avgScore = employeeEntry.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
                avgMap.put(employeeEntry.getKey(), new BigDecimal(avgScore).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            indicatorEmployeeAvgScores.put(entry.getKey(), avgMap);
        }

        // 评委（某一位）打分的平均成绩（评委所有评分的平均成绩）
        for (Map.Entry<String, List<Double>> entry : evaluatorScores.entrySet()) {
            double avgScore = entry.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
            evaluatorAvgScores.put(entry.getKey(), avgScore);
        }
        //所有评委在该项指标下的平均成绩(某个指标的平均成绩)
        for (Map.Entry<String, List<Double>> entry : indicatorScores.entrySet()) {
            double avgScore = entry.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
            indicatorAvgScores.put(entry.getKey(), avgScore);
        }
        //所有评委在该项指标下对员工的平均成绩（一个员工在某个指标下的某个评委的平均成绩）
        for (Map.Entry<String, Map<String, Map<String, List<Double>>>> entry : employeeIndicatorEvaluatorScores.entrySet()) {
            HashMap<String, Map<String, Double>> indicatorMap = new HashMap<>();
            for (Map.Entry<String, Map<String, List<Double>>> indicatorEntry : entry.getValue().entrySet()) {
                HashMap<String, Double> evaluatorMap = new HashMap<>();
                for (Map.Entry<String, List<Double>> evaluatorEntry : indicatorEntry.getValue().entrySet()) {
                    double avgScore = evaluatorEntry.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
                    evaluatorMap.put(evaluatorEntry.getKey(), avgScore);
                }
                indicatorMap.put(indicatorEntry.getKey(), evaluatorMap);
            }
            employeeIndicatorEvaluatorAvgScores.put(entry.getKey(), indicatorMap);
        }
    }


    public List<Rating> newEvaluationToRating(List<NewEvaluationExcel> newEvaluationExcels){
        List<Rating> ratings = new ArrayList<>();
        for (NewEvaluationExcel newEvaluationExcel : newEvaluationExcels) {
            //政治素质
            if (newEvaluationExcel.getPolitics()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"政治素质",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getPolitics());
                ratings.add(rating);
            }
            //专业知识
            if (newEvaluationExcel.getProfessional()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"专业知识",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getProfessional());
                ratings.add(rating);
            }
            //沟通协调
            if (newEvaluationExcel.getCommunication()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"沟通协调",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getCommunication());
                ratings.add(rating);
            }
            //敬业精神
            if (newEvaluationExcel.getDedicated()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"敬业精神",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getDedicated());
                ratings.add(rating);
            }
            //全局思维
            if (newEvaluationExcel.getThinking()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"全局思维",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getThinking());
                ratings.add(rating);
            }
            //团队管理
            if (newEvaluationExcel.getManagement()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"团队管理",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getManagement());
                ratings.add(rating);
            }
            //问题解决
            if (newEvaluationExcel.getSolve()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"问题解决",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getSolve());
                ratings.add(rating);
            }
            //学习能力
            if (newEvaluationExcel.getStudy()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"学习能力",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getStudy());
                ratings.add(rating);
            }
            //应变处置
            if (newEvaluationExcel.getDispose()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"应变处置",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getDispose());
                ratings.add(rating);
            }
            //责任意识
            if (newEvaluationExcel.getDuty()!=null) {
                Rating rating = new Rating(newEvaluationExcel.getEmployeeCode(),
                        newEvaluationExcel.getEmployeeName(),newEvaluationExcel.getEmployeeCompany(),
                        newEvaluationExcel.getEmployeeDept(),newEvaluationExcel.getEmployeePost(),
                        newEvaluationExcel.getEmployeePostType(),"责任意识",
                        newEvaluationExcel.getEvaluatorName(),newEvaluationExcel.getDuty());
                ratings.add(rating);
            }
            this.emplyeeCompanyMap.put(newEvaluationExcel.getEmployeeCode(),newEvaluationExcel.getEmployeeCompany());
        }
        return ratings;
    }

    public NewAvgDataExcel getAvgData(String employeeCode){
        for (NewAvgDataExcel avgDataExcel : avgDataExcels) {
            if (avgDataExcel.getEmployeeCode().equals(employeeCode)){
                return avgDataExcel;
            }
        }
        return null;
    }

    private Rating getRating(String employeeCode) {
        for (Rating rating : ratings) {
            if (rating.getEmployeeCode().equals(employeeCode)){
                return rating;
            }
        }
        return null;
    }

    // 输出平均分数据
    private void outputAvgData(String outPath) {
        System.out.println("---- 某个指标下某个员工的平均成绩 ----");
        for (Map.Entry<String, Map<String, Double>> indicatorEntry : indicatorEmployeeAvgScores.entrySet()) {
            for (Map.Entry<String, Double> employeeEntry : indicatorEntry.getValue().entrySet()) {
                String employeeCode = employeeEntry.getKey();
                String indicator = indicatorEntry.getKey();
                Double avgScore = employeeEntry.getValue();
                if (avgDataExcels!=null&&avgDataExcels.size()>0) {
                    NewAvgDataExcel avgData = getAvgData(employeeCode);
                    if (avgData==null) {
                        Rating rating = getRating(employeeCode);
                        NewAvgDataExcel avgDataExcel = new NewAvgDataExcel();
                        avgDataExcel.setEmployeeCode(employeeCode);
                        //单位
                        avgDataExcel.setEmployeeCompany(rating.getEmployeeCompany());
                        //部门
                        avgDataExcel.setEmployeeDept(rating.getEmployeeDept());
                        //姓名
                        avgDataExcel.setEmployeeName(rating.getEmployeeName());
                        //岗位
                        avgDataExcel.setEmployeePost(rating.getEmployeePost());
                        //岗位类型
                        avgDataExcel.setEmployeePostType(rating.getEmployeePostType());
                        //沟通协调
                        if (indicator.equals("沟通协调")){
                            avgDataExcel.setCommunication(avgScore);
                        }else if (indicator.equals("敬业精神")){
                            avgDataExcel.setDedicated(avgScore);
                        }else if (indicator.equals("全局思维")){
                            avgDataExcel.setThinking(avgScore);
                        }else if (indicator.equals("团队管理")){
                            avgDataExcel.setManagement(avgScore);
                        }else if (indicator.equals("问题解决")){
                            avgDataExcel.setSolve(avgScore);
                        }else if (indicator.equals("学习能力")){
                            avgDataExcel.setStudy(avgScore);
                        }else if (indicator.equals("应变处置")){
                            avgDataExcel.setDispose(avgScore);
                        }else if (indicator.equals("责任意识")){
                            avgDataExcel.setDuty(avgScore);
                        }else if (indicator.equals("政治素质")){
                            avgDataExcel.setPolitical(avgScore);
                        }else if (indicator.equals("专业知识")){
                            avgDataExcel.setProfessional(avgScore);
                        }
                        avgDataExcels.add(avgDataExcel);
                    }else {
                        //沟通协调
                        if (indicator.equals("沟通协调")){
                            avgData.setCommunication(avgScore);
                        }else if (indicator.equals("敬业精神")){
                            avgData.setDedicated(avgScore);
                        }else if (indicator.equals("全局思维")){
                            avgData.setThinking(avgScore);
                        }else if (indicator.equals("团队管理")){
                            avgData.setManagement(avgScore);
                        }else if (indicator.equals("问题解决")){
                            avgData.setSolve(avgScore);
                        }else if (indicator.equals("学习能力")){
                            avgData.setStudy(avgScore);
                        }else if (indicator.equals("应变处置")){
                            avgData.setDispose(avgScore);
                        }else if (indicator.equals("责任意识")){
                            avgData.setDuty(avgScore);
                        }else if (indicator.equals("政治素质")){
                            avgData.setPolitical(avgScore);
                        }else if (indicator.equals("专业知识")){
                            avgData.setProfessional(avgScore);
                        }
                        avgDataExcels = avgDataExcels.stream().filter(o->
                                !(o.getEmployeeCode().equals(employeeCode))).collect(Collectors.toList());
                        avgDataExcels.add(avgData);
                    }
                }else {
                    NewAvgDataExcel avgDataExcel = new NewAvgDataExcel();
                    avgDataExcel.setEmployeeCode(employeeCode);
                    Rating rating = getRating(employeeCode);
                    //单位
                    avgDataExcel.setEmployeeCompany(rating.getEmployeeCompany());
                    //部门
                    avgDataExcel.setEmployeeDept(rating.getEmployeeDept());
                    //姓名
                    avgDataExcel.setEmployeeName(rating.getEmployeeName());
                    //岗位
                    avgDataExcel.setEmployeePost(rating.getEmployeePost());
                    //岗位类型
                    avgDataExcel.setEmployeePostType(rating.getEmployeePostType());
                    //沟通协调
                    if (indicator.equals("沟通协调")){
                        avgDataExcel.setCommunication(avgScore);
                    }else if (indicator.equals("敬业精神")){
                        avgDataExcel.setDedicated(avgScore);
                    }else if (indicator.equals("全局思维")){
                        avgDataExcel.setThinking(avgScore);
                    }else if (indicator.equals("团队管理")){
                        avgDataExcel.setManagement(avgScore);
                    }else if (indicator.equals("问题解决")){
                        avgDataExcel.setSolve(avgScore);
                    }else if (indicator.equals("学习能力")){
                        avgDataExcel.setStudy(avgScore);
                    }else if (indicator.equals("应变处置")){
                        avgDataExcel.setDispose(avgScore);
                    }else if (indicator.equals("责任意识")){
                        avgDataExcel.setDuty(avgScore);
                    }else if (indicator.equals("政治素质")){
                        avgDataExcel.setPolitical(avgScore);
                    }else if (indicator.equals("专业知识")){
                        avgDataExcel.setProfessional(avgScore);
                    }
                    avgDataExcels.add(avgDataExcel);
                }
            }
        }
        ExcelUtil<NewAvgDataExcel> oldUtil = new ExcelUtil<>(NewAvgDataExcel.class);
        oldUtil.exportExcel(avgDataExcels,"各员工平均成绩",null,outPath);
        System.out.println("---- 评委（某一位）打分的平均成绩 ----");
        for (Map.Entry<String, Double> evaluatorEntry : evaluatorAvgScores.entrySet()) {
            System.out.printf("评委: %s, 平均成绩: %.2f%n",
                    evaluatorEntry.getKey(), evaluatorEntry.getValue());
        }
        System.out.println("---- 某一指标的平均成绩 ----");
        for (Map.Entry<String, Double> indicatorEntry : indicatorAvgScores.entrySet()) {
            System.out.printf("指标: %s, 平均成绩: %.2f%n",
                    indicatorEntry.getKey(), indicatorEntry.getValue());
        }
        System.out.println("----         清洗后的数据        ----");
        for (Rating rating : ratings) {
            System.out.printf("编号: %s,员工: %s, 评委: %s, 指标: %s, 评分: %.2f%n",
                    rating.getEmployeeCode(),rating.getEmployeeName(), rating.getEvaluatorName(), rating.getIndicator(), rating.getScore());
        }
    }



    public static void main(String[] args) throws IOException {
        ExcelUtil<NewEvaluationExcel> util = new ExcelUtil<>(NewEvaluationExcel.class);
        MultipartFile file = util.convertFileToMultipartFile("D:\\桌面\\datav\\四川复烤\\真实数据\\评价分.xlsx");
        List<NewEvaluationExcel> newEvaluationExcels = util.importExcel(file.getInputStream());
        // 初始化计算平均分对象
        NewAvgData evaluationData = new NewAvgData(newEvaluationExcels);
        evaluationData.outputAvgData("D:\\桌面\\datav\\四川复烤\\真实数据\\各员工平均成绩.xlsx");
    }
}
