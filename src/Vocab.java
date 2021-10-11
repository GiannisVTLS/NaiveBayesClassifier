import java.util.ArrayList;

public class Vocab{
    private ArrayList<String> vocab;
    private ArrayList<Integer> wordCount;


    public Vocab(ArrayList<String> voc) {
        vocab = new ArrayList<>(voc.size());
        wordCount = new ArrayList<>(voc.size());
        for(int i=0; i < voc.size(); i++){
            vocab.add(voc.get(i));
            wordCount.add(0);
        }
    }

    public void addCount(int index, int freq) {
        wordCount.set(index , wordCount.get(index) + freq);
    }

    public ArrayList<String> getVocab() {
        return vocab;
    }

    public ArrayList<Integer> getWordCount() {
        return wordCount;
    }

}
