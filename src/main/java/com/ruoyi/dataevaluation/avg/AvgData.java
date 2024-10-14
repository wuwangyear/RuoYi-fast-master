package com.ruoyi.dataevaluation.avg;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.dataevaluation.main.OldEvaluationExcel;
import com.ruoyi.dataevaluation.main.Rating;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 计算员工指标平均分
 */
public class AvgData {
    List<Rating> ratings;
    List<AvgDataExcel> avgDataExcels = new ArrayList<>();
    Map<String,String> emplyeeCompanyMap = new HashMap<>();//员工单位
    Map<String, Map<String, Double>> indicatorEmployeeAvgScores = new HashMap<>();//每个指标下员工的平均成绩
    Map<String, Double> evaluatorAvgScores = new HashMap<>();//评委（某一位）打分的平均成绩
    Map<String, Double> indicatorAvgScores = new HashMap<>();//所有评委在该项指标下的平均成绩
    Map<String, Map<String, Map<String, Double>>> employeeIndicatorEvaluatorAvgScores = new HashMap<>();//所有评委在该项指标下对员工的平均成绩

    AvgData(List<OldEvaluationExcel> oldEvaluationExcels) {
        List<Rating> ratings = oldEvaluationToRating(oldEvaluationExcels);
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
                    .computeIfAbsent(rating.getEmployeeName(), k -> new ArrayList<>()).add(rating.getScore());
            //某一位的所有评分
            evaluatorScores.computeIfAbsent(rating.getEvaluatorName(), k -> new ArrayList<>()).add(rating.getScore());
            //所有评委在某指标下评分
            indicatorScores.computeIfAbsent(rating.getIndicator(), k -> new ArrayList<>()).add(rating.getScore());
            //所有评委在该指标下对员工的评分
            employeeIndicatorEvaluatorScores.computeIfAbsent(rating.getEmployeeName(), k -> new HashMap<>())
                    .computeIfAbsent(rating.getIndicator(), k -> new HashMap<>())
                    .computeIfAbsent(rating.getEvaluatorName(), k -> new ArrayList<>()).add(rating.getScore());
        }

        // 计算每个员工在每个指标下的平均分数（一个指标下某个员工的平均分数）
        for (Map.Entry<String, Map<String, List<Double>>> entry : indicatorEmployeeScores.entrySet()) {
            Map<String, Double> avgMap = new HashMap<>();
            for (Map.Entry<String, List<Double>> employeeEntry : entry.getValue().entrySet()) {
                double avgScore = employeeEntry.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
                avgMap.put(employeeEntry.getKey(), avgScore);
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


    public List<Rating> oldEvaluationToRating(List<OldEvaluationExcel> oldEvaluationExcels){
        List<Rating> ratings = new ArrayList<>();
        for (OldEvaluationExcel oldEvaluationExcel : oldEvaluationExcels) {
            if (StringUtils.isEmpty(oldEvaluationExcel.getEvaluatorName())) {
                continue;
            }
            if (StringUtils.isEmpty(oldEvaluationExcel.getEmployeeCompany())) {
                continue;
            }
            if (StringUtils.isEmpty(oldEvaluationExcel.getEmployeeName())) {
                continue;
            }
            if (oldEvaluationExcel.getCommunication()!=null) {
                Rating rating1 = new Rating(oldEvaluationExcel.getEmployeeName(),oldEvaluationExcel.getEmployeeCompany(),"沟通协调",oldEvaluationExcel.getEvaluatorName(),oldEvaluationExcel.getCommunication());
                ratings.add(rating1);
            }
            if (oldEvaluationExcel.getDedicated()!=null) {
                Rating rating2 = new Rating(oldEvaluationExcel.getEmployeeName(),oldEvaluationExcel.getEmployeeCompany(),"敬业精神",oldEvaluationExcel.getEvaluatorName(),oldEvaluationExcel.getDedicated());
                ratings.add(rating2);
            }
            if (oldEvaluationExcel.getThinking()!=null) {
                Rating rating3 = new Rating(oldEvaluationExcel.getEmployeeName(),
                        oldEvaluationExcel.getEmployeeCompany(),"全局思维",
                        oldEvaluationExcel.getEvaluatorName(),oldEvaluationExcel.getThinking());
                ratings.add(rating3);
            }
            if (oldEvaluationExcel.getManagement()!=null) {
                Rating rating4 = new Rating(oldEvaluationExcel.getEmployeeName(),
                        oldEvaluationExcel.getEmployeeCompany(),"团队管理",
                        oldEvaluationExcel.getEvaluatorName(),oldEvaluationExcel.getManagement());
                ratings.add(rating4);
            }
            if (oldEvaluationExcel.getSolve()!=null) {
                Rating rating5 = new Rating(oldEvaluationExcel.getEmployeeName(),
                        oldEvaluationExcel.getEmployeeCompany(),"问题解决",
                        oldEvaluationExcel.getEvaluatorName(),oldEvaluationExcel.getSolve());
                ratings.add(rating5);
            }
            if (oldEvaluationExcel.getStudy()!=null) {
                Rating rating6 = new Rating(oldEvaluationExcel.getEmployeeName(),
                        oldEvaluationExcel.getEmployeeCompany(),"学习能力",
                        oldEvaluationExcel.getEvaluatorName(),oldEvaluationExcel.getStudy());
                ratings.add(rating6);
            }
            if (oldEvaluationExcel.getDispose()!=null) {
                Rating rating7 = new Rating(oldEvaluationExcel.getEmployeeName(),
                        oldEvaluationExcel.getEmployeeCompany(),"应变处置",
                        oldEvaluationExcel.getEvaluatorName(),oldEvaluationExcel.getDispose());
                ratings.add(rating7);
            }
            if (oldEvaluationExcel.getDuty()!=null) {
                Rating rating8 = new Rating(oldEvaluationExcel.getEmployeeName(),
                        oldEvaluationExcel.getEmployeeCompany(),"责任意识",
                        oldEvaluationExcel.getEvaluatorName(),oldEvaluationExcel.getDuty());
                ratings.add(rating8);
            }
            this.emplyeeCompanyMap.put(oldEvaluationExcel.getEmployeeName(),oldEvaluationExcel.getEmployeeCompany());
        }
        return ratings;
    }

    public AvgDataExcel getAvgData(String employeeName){
        for (AvgDataExcel avgDataExcel : avgDataExcels) {
            if (avgDataExcel.getEmployeeName().equals(employeeName)){
                return avgDataExcel;
            }
        }
        return null;
    }

    // 输出平均分数据
    private void outputAvgData(String outPath) {
        System.out.println("---- 某个指标下某个员工的平均成绩 ----");
        for (Map.Entry<String, Map<String, Double>> indicatorEntry : indicatorEmployeeAvgScores.entrySet()) {
            for (Map.Entry<String, Double> employeeEntry : indicatorEntry.getValue().entrySet()) {
                String employeeName = employeeEntry.getKey();
                String indicator = indicatorEntry.getKey();
                Double avgScore = employeeEntry.getValue();
                if (avgDataExcels!=null&&avgDataExcels.size()>0) {
                    AvgDataExcel avgData = getAvgData(employeeName);
                    if (avgData==null) {
                        AvgDataExcel avgDataExcel = new AvgDataExcel();
                        avgDataExcel.setEmployeeName(employeeName);
                        avgDataExcel.setEmployeeCompany(emplyeeCompanyMap.get(employeeName));
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
                        }
                        avgDataExcels = avgDataExcels.stream().filter(o->
                                !(o.getEmployeeName().equals(employeeName))).collect(Collectors.toList());
                        avgDataExcels.add(avgData);
                    }
                }else {
                    AvgDataExcel avgDataExcel = new AvgDataExcel();
                    avgDataExcel.setEmployeeName(employeeName);
                    avgDataExcel.setEmployeeCompany(emplyeeCompanyMap.get(employeeName));
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
                    }
                    avgDataExcels.add(avgDataExcel);
                }
            }
        }
        ExcelUtil<AvgDataExcel> oldUtil = new ExcelUtil<>(AvgDataExcel.class);
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
            System.out.printf("员工: %s, 评委: %s, 指标: %s, 评分: %.2f%n",
                    rating.getEmployeeName(), rating.getEvaluatorName(), rating.getIndicator(), rating.getScore());
        }
    }

    public static void main(String[] args) throws IOException {
        ExcelUtil<OldEvaluationExcel> util = new ExcelUtil<>(OldEvaluationExcel.class);
        MultipartFile file = util.convertFileToMultipartFile("D:\\桌面\\datav\\四川复烤\\真实数据\\清洗后的无领导小组讨论（脱敏数据）.xlsx");
        List<OldEvaluationExcel> oldEvaluationExcels = util.importExcel(file.getInputStream());
        // 初始化计算平均分对象
        AvgData evaluationData = new AvgData(oldEvaluationExcels);
        evaluationData.outputAvgData("D:\\桌面\\datav\\四川复烤\\真实数据\\清洗后的无领导小组讨论（脱敏数据）的各员工平均成绩.xlsx");
    }
}
