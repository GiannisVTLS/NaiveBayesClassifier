import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class DevTestD {
    private ArrayList<String> positive;
    private ArrayList<String> negative;
    private int trainCap;
    private float threshold;


    public DevTestD(String info) {
        positive = new ArrayList<>();
        negative = new ArrayList<>();
        if(info.equals("dev")){
            positive = readReviews("devP");
            negative = readReviews("devN");
        }else{
            positive = readReviews("Pos");
            negative = readReviews("Neg");
        }

    }

    public DevTestD(String info, int number) {
        positive = new ArrayList<>();
        negative = new ArrayList<>();
        trainCap = number + 1249;
        if (info.equals("train")) {
            positive = readReviews("trainP");
            negative = readReviews("trainN");
        }
    }

    public ArrayList<String> getPositive() {
        return positive;
    }

    public ArrayList<String> getNegative() {
        return negative;
    }

    public float calculate(TrainD train){
        int truePos = testData(positive, train, "P");
        int falseNeg = positive.size() - truePos;
        int trueNeg = testData(negative, train, "N");
        int falsePos = negative.size() - trueNeg;
        return (float)((truePos + trueNeg)*100)/(truePos + trueNeg + falseNeg + falsePos);
    }

    public void calculateTest(TrainD train, float threshold) {
        this.threshold = threshold;
        int truePos = testData(positive, train, "P");
        int falseNeg = positive.size() - truePos;
        int trueNeg = testData(negative, train, "N");
        int falsePos = negative.size() - trueNeg;
        float acc = (float)((truePos + trueNeg)*100)/(truePos + trueNeg + falseNeg + falsePos);
        float precision = (float)truePos*100 / (truePos+falsePos);
        float recall = (float)truePos*100 / (truePos + falseNeg);
        float f1Score = (float)2*precision*recall/(precision + recall);
        if(threshold == 0) {
            System.out.println("\nAcc: " +acc+"%\n" + "Precision: " + precision + "%\n" + "Recall: " + recall + "%\n" + "F1 Score: " + f1Score + "%\n");
        }
        else {
            System.out.println("For threshold: " + threshold + "\nPrecision: " + precision + "%\n" + "Recall: " + recall + "%\n" + "F1 Score: " + f1Score + "%\n");
        }
    }


    public int testData(ArrayList<String> review, TrainD train, String flag) {
        int number = 0;
        for (int i=0; i<review.size(); i++) {
            String[] arr = review.get(i).split("\\W+");

            int index;
            double probP = 0;
            double probN = 0;

            for (int j = 0; j < arr.length; j++) {
                if (train.getPos().getVocab().contains(arr[j].toLowerCase())) {
                    index = train.getPos().getVocab().indexOf(arr[j].toLowerCase());
                    probP = probP + Math.log((double) (train.getPos().getWordCount().get(index)+1) / (train.getTotalPos() + train.getTotal()));
                    probN = probN + Math.log((double) (train.getNeg().getWordCount().get(index)+1) / (train.getTotalNeg() + train.getTotal()));
                }else{
                    probP = Math.log(1.0 / (train.getTotalPos() + train.getTotal())) + probP;
                    probN = Math.log(1.0 / (train.getTotalNeg() + train.getTotal())) + probN;
                }
            }
            if(flag.equals("P")) {
                if (probP > probN && threshold == 0) {
                    number += 1;
                }
                if (probP> -threshold && threshold != 0) {
                    number += 1;
                }
            }else{
                if (probN > probP && threshold == 0) {
                    number += 1;
                }
                if (probN< -threshold && threshold != 0) {
                    number += 1;
                }
            }
        }

        return number;
    }

    public ArrayList<String> readReviews(String aff) {
        ArrayList<String> review = new ArrayList<>();
        try {
            File myObj = null;
            File[] listofFiles;
            Scanner myReader;
            switch (aff) {
                case "Pos" -> myObj = new File(".\\aclImdb\\test\\pos");
                case "Neg" -> myObj = new File(".\\aclImdb\\test\\neg");
                case "devP", "trainP" -> myObj = new File(".\\aclImdb\\train\\pos");
                case "devN", "trainN" -> myObj = new File(".\\aclImdb\\train\\neg");
            }
            assert myObj != null;
            listofFiles = myObj.listFiles();
            assert listofFiles != null;
            Arrays.sort(listofFiles, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    try {
                        int i1 = Integer.parseInt(f1.getName().split("_")[0]);
                        int i2 = Integer.parseInt(f2.getName().split("_")[0]);
                        return i1 - i2;
                    } catch(NumberFormatException e) {
                        throw new AssertionError(e);
                    }
                }
            });

            int counterD =0;
            int counterT =0;
            if(aff.equals("devP")||aff.equals("devN")) {
                for (File file : listofFiles) {
                    counterD+=1;
                    if (file.isFile()) {
                        myReader = new Scanner(file);
                        String rev = ".";
                        while (myReader.hasNext()) {
                            String add = myReader.nextLine();
                            rev = rev.concat("." + add);
                        }
                        if(!rev.equals(".")) review.add(rev);
                        if(counterD >= 1250) {
                            myReader.close();
                            break;
                        }
                        myReader.close();
                    }
                }
            }
            if(aff.equals("trainP")||aff.equals("trainN")) {
                for (File file : listofFiles) {
                    myReader = new Scanner(file);
                    if (file.isFile() && counterT >= 1250) {
                        String rev = ".";
                        while (myReader.hasNextLine()) {
                            String add = myReader.nextLine();
                            rev = rev.concat("." + add);
                        }
                        if(!rev.equals(" ")) review.add(rev);
                        if(counterT >= trainCap) {
                            myReader.close();
                            break;
                        }
                        myReader.close();
                    }
                    counterT++;
                }
            }
            if(aff.equals("Pos")||aff.equals("Neg")) {
                for (File file : listofFiles) {
                    myReader = new Scanner(file);
                    String rev = ".";
                    while (myReader.hasNextLine()) {
                        String add = myReader.nextLine();
                        rev = rev.concat("." + add);
                    }
                    if(!rev.equals(".")) review.add(rev);
                    myReader.close();

                }
            }
            return review;

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }
}
