package ru.dingo.apiKeyRotator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class TimeCondition {
    int count;
    int seconds;

    public TimeCondition(int count, int seconds) {
        this.count = count;
        this.seconds = seconds;
    }


    static public ArrayList<TimeCondition> parseTimeConditions(String timeConditionsStr) {
        ArrayList<TimeCondition> timeConditions = new ArrayList<>();
        String[] timeConditionsArr = timeConditionsStr.split(",");
        for (String timeCondition : timeConditionsArr) {
            String[] timeConditionArr = timeCondition.split("/");
            if (timeConditionArr.length != 2) {
                continue;
            }
            timeConditions.add(new TimeCondition(Integer.parseInt(timeConditionArr[0]), Integer.parseInt(timeConditionArr[1])));
        }
        return timeConditions;
    }

    static public String timeConditionsToString(ArrayList<TimeCondition> timeConditions) {
        StringBuilder timeConditionsStr = new StringBuilder();
        for (TimeCondition timeCondition : timeConditions) {
            timeConditionsStr.append(timeCondition.getCount()).append("/").append(timeCondition.getSeconds()).append(",");
        }
        if (timeConditionsStr.length() > 0) {
            timeConditionsStr.deleteCharAt(timeConditionsStr.length() - 1);
        }

        return timeConditionsStr.toString();
    }
}
