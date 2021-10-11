import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TrainD {

    private Vocab pos;
    private Vocab neg;
    private int totalPos;
    private int totalNeg;
    private int total;

    public TrainD() {}

    public TrainD(ArrayList<String> voc) {
        pos = new Vocab(voc);
        neg = new Vocab(voc);
    }

    public Vocab getPos() {
        return pos;
    }

    public Vocab getNeg() {
        return neg;
    }

    public int getTotalPos() {
        return totalPos;
    }

    public int getTotalNeg() {
        return totalNeg;
    }

    public int getTotal() {
        return total;
    }

    public void readTrain(int j) {
        try {
            File myObj = new File(".\\aclImdb\\train\\labeledBow.feat");
            Scanner myReader = new Scanner(myObj);
            int counterDev = 0;
            int counterTrain = 0;
            while (myReader.hasNextLine()) {
                String review = myReader.nextLine();
                String[] splitted = review.split(" ");
                if (Integer.parseInt(splitted[0]) >= 7) {
                    if (counterDev < 1250) {
                        counterDev += 1;
                    } else if(counterTrain < j){
                        for (int i = 1; i < splitted.length; i++) {
                            String[] splitToWord = splitted[i].split(":");
                            pos.addCount(Integer.parseInt(splitToWord[0]), Integer.parseInt(splitToWord[1]));
                        }
                        counterTrain++;
                    }
                } else {
                    if (counterDev < 2500) {
                        counterDev += 1;
                    } else if(counterTrain < 2*j){
                        for (int i = 1; i < splitted.length; i++) {
                            String[] splitToWord = splitted[i].split(":");
                            neg.addCount(Integer.parseInt(splitToWord[0]), Integer.parseInt(splitToWord[1]));
                        }
                        counterTrain+=1;
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void trainVocab() {
        totalPos=0;
        totalNeg=0;
        total = 0;
        for(int i = 0; i < pos.getVocab().size();i++){
            totalPos = pos.getWordCount().get(i)+totalPos;
            totalNeg = neg.getWordCount().get(i)+totalNeg;
            if(pos.getWordCount().get(i)!=0 || neg.getWordCount().get(i)!=0) total++;
        }
    }

    public void removeUninformative(int n, int m) {
        for (int i=0; i< n; i++){
            pos.getVocab().remove(0);
            pos.getWordCount().remove(0);
            neg.getVocab().remove(0);
            neg.getWordCount().remove(0);
        }
        int y= pos.getVocab().size();
        for (int i=m; i< y; i++){
            pos.getVocab().remove(m);
            pos.getWordCount().remove(m);
            neg.getVocab().remove(m);
            neg.getWordCount().remove(m);
        }
    }
}
