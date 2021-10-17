import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class Main {
    protected static ArrayList<String> names;
    protected static ArrayList<Integer>[] list;
    protected static ArrayList<Double>[] prob0;
    protected static ArrayList<Double>[] prob1;
    public static void main(String[] args) throws FileNotFoundException{
        Scanner scan = new Scanner(System.in);
        System.out.print("Name of training data file: ");
        String trainFileName = scan.nextLine();
        System.out.print("Name of test data file: ");
        String testFileName = scan.nextLine();
        Scanner trainFile = new Scanner(new File(trainFileName));
        String line = trainFile.nextLine();
        int count = 0;
        names = new ArrayList();           //arrayList of class names
        Scanner reader = new Scanner(line);
        //populates arrayList names with class names and counts total number of classes
        while(reader.hasNext()){
            names.add(reader.next());
            count++;
        }
        
        list = new ArrayList[count];   //array of arrayLists, where each array is a different class,
        for(int i = 0; i < count; i++) {                   //containing the different values of their respective class
            list[i] = new ArrayList<Integer>();
        }
        
        while(trainFile.hasNext()){
            line = trainFile.nextLine();
            Scanner chopper = new Scanner(line);
            for (int i = 0; i < count; i++) {
                list[i].add(chopper.nextInt());
            }
        }
        
        //creates arrays of test data
        Scanner tfile = new Scanner(new File(testFileName));
        String tline = tfile.nextLine();
        ArrayList<String> tnames = new ArrayList<String>();
        Scanner treader = new Scanner(tline);
        while(reader.hasNext()){
            tnames.add(reader.next());
        }
        ArrayList<Integer>[] tlist = new ArrayList[count];   //array of arrayLists, where each array is a different class,
        for (int i = 0; i < count; i++) {                   //containing the different values of their respective class
            tlist[i] = new ArrayList<Integer>();
        }
        //populates the values into each array
        while(tfile.hasNext()){
            tline = tfile.nextLine();
            Scanner tchopper = new Scanner(tline);
            for (int i = 0; i < count; i++) {
                tlist[i].add(tchopper.nextInt());
            }
        }
        //count of both binary values in class instance
        int sum0 = 0;
        int sum1 = 0;
        int totalSum = 0;
        for(int i = 0; i < list[count-1].size();i++) {
            if(list[count-1].get(i) == 0)
                sum0++;
            else if(list[count-1].get(i) == 1)
                sum1++;
            totalSum++;
        }
        
        //ArrayList<Integer>[2] prob will have 2 arrayLists which store
        //the different probabilities of each class instance
        //prob0[0] will contain P(A1 = x1|c1), P(A1 = x2|c1)
        //prob0[1] will contain P(A2 = y1|c1), P(A2 = y2|c1)
        //prob0[count-1] will contain P(c1)
        //prob1[0] will contain P(A1 = x1|c2), P(A1 = x2|c2)
        //prob0[1] will contain P(A2 = y1|c2), P(A2 = y2|c2)
        //prob1[count-1] will contain P(c2)
        
        prob0 = new ArrayList[count];
        prob1 = new ArrayList[count];
        for(int i = 0; i < count; i++) {                   //containing the different values of their respective class
            prob0[i] = new ArrayList<Double>();
            prob1[i] = new ArrayList<Double>();
        }
        
        
        //calculate probabilities of c1 and c2
        prob0[count-1].add(calculateProbability(count-1, 0));
        prob1[count-1].add(calculateProbability(count-1, 1));
        
        //calculate probabilities of instances given c1
        for(int i = 0; i < count-1; i++) {
            prob0[i].add(calculateProbabilityGiven(i, 0, count-1, 0));
            prob0[i].add(calculateProbabilityGiven(i, 1, count-1, 0));
        }
        
        //calculate probabilities of instances given c2
        for(int i = 0; i < count-1; i++) {
            prob1[i].add(calculateProbabilityGiven(i, 0, count-1, 1));
            prob1[i].add(calculateProbabilityGiven(i, 1, count-1, 1));
        }
        
        //print probabilities

        //print P(c1)
        System.out.print("P(class= 0)=" + prob0[count-1].get(0) + " ");
        //print P(instance|c1)
        for(int i = 0; i < count-1; i++) {
            System.out.print("P(" + names.get(i) + "=0|0)=" + prob0[i].get(0) + " ");
            System.out.print("P(" + names.get(i) + "=1|0)=" + prob0[i].get(1) + " ");
        }
        System.out.println();
        //print P(c1)
        System.out.print("P(class= 1)=" + prob1[count-1].get(0) + " ");
        //print P(instance|c1)
        for(int i = 0; i < count-1; i++) {
            System.out.print("P(" + names.get(i) + "=0|1)=" + prob1[i].get(0) + " ");
            System.out.print("P(" + names.get(i) + "=1|1)=" + prob1[i].get(1) + " ");
        }
        System.out.println();

        //use probability data on training data
        double trainingAccuracy = testData(list);
        //use probability data on testing data
        System.out.println("Accuracy on training set (" + list[0].size() + " instances): " + trainingAccuracy);
        double testingAccuracy = testData(tlist);
        System.out.println("Accuracy on testing set (" + tlist[0].size() + " instances): " + testingAccuracy);
        

    }
    //tests array of arraylists(training or test data) and calculates the accuracy of the determined classifier
    public static double testData(ArrayList<Integer>[] list) {
        double answer = 0;
        int numRight = 0;
        int total = 0;
        double c1Prob = 1;
        double c2Prob = 1;
        for(int i = 0; i < list[0].size(); i++) {

            for(int u = 0; u < list.length; u++) {
                c1Prob *= getProbability(u, list[u].get(i), 0); //multiplying P(instance|c1) to existing product
                c2Prob *= getProbability(u, list[u].get(i), 1); //multiplying P(instance|c2) to existing product
            }
            if(c1Prob >= c2Prob && list[list.length-1].get(i) == 0) //if c1 branch in naive bayes calculation is correct, note
                numRight++;
            else if(c2Prob > c1Prob && list[list.length-1].get(i) == 1) //if c2 branch in naive bayes calculation is correct, note
                numRight++;
            c1Prob = 1;
            c2Prob = 1;
            total++;
        }
        answer = ((double) numRight) / total;
        answer *= 100;
        answer = Math.round(answer * 100.0) / 100.0;
        return answer;
    }
    //find the correct probability from prob0 or prob1 given the column value, value in that column of the dataset, and value of C
    public static double getProbability(int index, int dataValue, int c) {
        double answer = 0;
        if(index == list.length-1)
        {
            if(c == 0)
                return prob0[index].get(0);
            else if(c == 1)
                return prob0[index].get(0);
        }
        else if(c == 0) {
            if(dataValue == 0)
                return prob0[index].get(0);
            else if(dataValue == 1)
                return prob0[index].get(1);
        }
        else if(c == 1) {
            if(dataValue == 0)
                return prob1[index].get(0);
            else if(dataValue == 1)
                return prob1[index].get(1);
        }
        System.out.println("Something wrong occured in getProbability");
        return answer;
    }
    public static double calculateProbability(int index, int value) {
        double answer = 0;
        int num = 0;
        int den = 0;
        for(int i = 0; i < list[index].size(); i++) {
            den++;
            if(list[index].get(i) == value)
                num++;
        }
        if(den != 0)
            answer = ((double)num)/den;
        
        answer = Math.round(answer * 100.0) / 100.0;
        return answer;
    }
    
    public static double calculateProbabilityGiven(int index, int value, int givenIndex, int valueGiven) {
        double answer = 0;
        int sumValue = 0;
        int sumGiven =0;
        int sumTotal = 0;
        for(int i = 0; i < list[givenIndex].size(); i++) {
            sumTotal++;
            if(list[givenIndex].get(i) == valueGiven) {
                sumGiven++;
                if(list[index].get(i) == value)
                    sumValue++;
            }
        }
        if(sumGiven != 0)
            answer = ((double)sumValue) / sumGiven;
        
        answer = Math.round(answer * 100.0) / 100.0;
        return answer;
    }
    
}