import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class AI {
    static ArrayList<String> voc = new ArrayList();
    static int n=75, m=2000;

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        System.out.println("Loading...");
        readVocab();
        TrainD train;
        float bestAcc = (float)0.0;
        int bestSize=0;


        DevTestD dev = new DevTestD("dev");
        for(int i=125; i<=12500; i=i+2500){
            train = new TrainD(voc);
            train.readTrain(i);
            train.removeUninformative(n,m);
            train.trainVocab();
            DevTestD trainData = new DevTestD("train", i);
            float trainAcc = trainData.calculate(train);
            float devAcc = dev.calculate(train);

            System.out.println("For "+ 2*i +" total training data\n" + "Dev accuracy: " + devAcc +"%" + "\nTrain accuracy: " + trainAcc + "%");
            if(devAcc > bestAcc){
                bestAcc = devAcc;
                bestSize = i;
            }
            if(i==125) i = 0;
        }
        System.out.println("Chose: " + 2*bestSize + " samples from training data");

        train = new TrainD(voc);
        train.readTrain(bestSize);
        train.removeUninformative(n,m);
        train.trainVocab();
        DevTestD test = new DevTestD("test");
        test.calculateTest(train, 0);
        for(float i=(float)1500; i<=5000; i=i+(float)1500) test.calculateTest(train, i);

        long endTime   = System.nanoTime();
        long totalTime = (endTime - startTime)/1000000000;
        System.out.println(totalTime + " seconds");
//
    }


    private static void readVocab() {
        try {
            File myObj = new File(".\\aclImdb\\imdb.vocab");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                voc.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
