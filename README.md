# NaiveBayesClassifier
Project for the Artificial Intelligence course at AUEB. Implemented the Naive Bayes Classifier with Laplace estimator, to classify texts into two categories (positive review/negative review).

<h2>Description</h2>

The task of the project was to implement learning algorithms, so that they can be used to classify texts into two (unrelated) categories (e.g. positive/negative opinion).

The vocabulary includes the "m" most frequent words in the training data (or the whole dataset), skipping the n most frequent words first. Property selection via information gain calculation (or other means) in the naive Bayes classifier is added using the Laplace estimator.

The report contains the results of the experiments performed on my implementation on this dataset, showing
<ul>
  <li>Learning curves showing the accuracy rate of the training data (as many as have been used at a time) and test data as a function of the number of training examples used in each iteration of the experiment.
  <li>Corresponding curves with precision, recall and F1 results as a function of the number of training examples.
</ul>

<h2>How to</h2>
<ol>
<li>Clone the project
<li>Download the <a href="https://ai.stanford.edu/~amaas/data/sentiment/">IMDB Dataset</a>
<li>Extract the contents in the "aclImdb" folder
<li>Run the following file using your preffered code editor (produced and tested with Intellij IDEA)
<pre>AI.java</pre>
<li>The results are shown in the "report.pdf" file.
</ol>
