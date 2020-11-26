package com.obiwanwheeler.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OptionGroup {

    private int graduatingIntervalInDays;
    private List<Integer> intervalSteps;
    private int correctAnswerIncreaseInDays;
    private int relapseDecreaseInDays;
    private int numberOfNewCardsToLearn;

    @JsonCreator
    public OptionGroup(@JsonProperty("graduatingIntervalInDays") int graduatingIntervalInDays,
                       @JsonProperty("intervalSteps") List<Integer> intervalSteps,
                       @JsonProperty("correctAnswerIncreaseInDays") int correctAnswerIncreaseInDays,
                       @JsonProperty("relapseDecreaseInDays") int relapseDecreaseInDays,
                       @JsonProperty("numberOfNewCardsToLearn") int numberOfNewCardsToLearn)
    {
        this.graduatingIntervalInDays = graduatingIntervalInDays;
        this.intervalSteps = intervalSteps;
        this.correctAnswerIncreaseInDays = correctAnswerIncreaseInDays;
        this.relapseDecreaseInDays = relapseDecreaseInDays;
        this.numberOfNewCardsToLearn = numberOfNewCardsToLearn;
    }

    public int getGraduatingIntervalInDays() {
        return graduatingIntervalInDays;
    }

    public void setGraduatingIntervalInDays(int graduatingIntervalInDays) {
        this.graduatingIntervalInDays = graduatingIntervalInDays;
    }

    public List<Integer> getIntervalSteps() {
        return intervalSteps;
    }

    public void setIntervalSteps(List<Integer> intervalSteps) {
        this.intervalSteps = intervalSteps;
    }

    public int getCorrectAnswerIncreaseInDays() {
        return correctAnswerIncreaseInDays;
    }

    public void setCorrectAnswerIncreaseInDays(int correctAnswerIncreaseInDays) {
        this.correctAnswerIncreaseInDays = correctAnswerIncreaseInDays;
    }

    public int getRelapseDecreaseInDays() {
        return relapseDecreaseInDays;
    }

    public void setRelapseDecreaseInDays(int relapseDecreaseInDays) {
        this.relapseDecreaseInDays = relapseDecreaseInDays;
    }

    public int getNumberOfNewCardsToLearn() {
        return numberOfNewCardsToLearn;
    }

    public void setNumberOfNewCardsToLearn(int numberOfNewCardsToLearn) {
        this.numberOfNewCardsToLearn = numberOfNewCardsToLearn;
    }
}
