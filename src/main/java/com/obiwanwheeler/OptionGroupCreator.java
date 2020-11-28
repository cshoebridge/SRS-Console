package com.obiwanwheeler;

import com.obiwanwheeler.objects.OptionGroup;
import com.obiwanwheeler.utilities.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OptionGroupCreator {

    Scanner scanner = new Scanner(System.in);

    public void createNewOptionGroup(){
        String optionGroupName = askForName();
        //TODO in FX give option to choose option group
        OptionGroup newOptionGroup = new OptionGroup.Builder().optionGroupName(optionGroupName).graduatingIntervalInDays(askForGraduatingInterval())
                                                    .intervalSteps(askForIntervalSteps()).correctAnswerIncreaseInDays(askForCorrectIncrease())
                                                    .relapseDecreaseInDays(askForRelapseDecrease()).numberOfNewCardsToLearn(askForNewCardsPerDay()).build();
        Serializer.SERIALIZER_SINGLETON.serializeToNew(newOptionGroup);
    }

    private String askForName(){
        System.out.print("what do you want to name the new option group? : ");
        return scanner.nextLine();
    }

    private int askForGraduatingInterval(){
        System.out.print("what do you want it's graduating interval to be? : ");
        int output = scanner.nextInt();
        scanner.nextLine();
        return output;
    }

    private List<Integer> askForIntervalSteps(){
        List<Integer> steps = new ArrayList<>();
        do {
            System.out.print("enter next interval step : ");
            steps.add(scanner.nextInt());
            scanner.nextLine();
            System.out.print("enter another step? (y/n) : ");
        } while(scanner.nextLine().equals("y"));
        return steps;
    }

    private int askForCorrectIncrease(){
        System.out.print("what do you want it's correct answer increase in days to be? : ");
        int output = scanner.nextInt();
        scanner.nextLine();
        return output;
    }

    private int askForRelapseDecrease(){
        System.out.print("what do you want it's relapse decrease in days to be? : ");
        int output = scanner.nextInt();
        scanner.nextLine();
        return output;
    }

    private int askForNewCardsPerDay(){
        System.out.print("how many new cards should be introduced each day? : ");
        int output = scanner.nextInt();
        scanner.nextLine();
        return output;
    }
}
