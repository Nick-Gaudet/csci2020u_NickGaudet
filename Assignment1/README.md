<h1>Bayesian Spam Detector</h1>
<h2>Information</h2>
<img src=".\spamImage.png">
<p>The Bayesian Spam Detector is a model of the Naive Bayes Spam filtering technique. First, it gets trained by evaluating 
e-mails that are spam and ham. Then creates an overall bag of words to be referenced by when testing actual spam 
and ham e-mails. The bag of words has all words mapped with a probability, signifying that a file with the given word
 will be spam. The closer the probability is to zero the less likely it is spam, the closer it is to 1.0 to more
 likely it is spam.</p>
 
 <h2>Improvements</h2>
<p>Accuracy and precision both are correlated to the words that are being evaluated to determine if a file is 
 spam. Knowing this, only the relevant words can be accounted for. The program starts with a default lower boundary of 
 0.27 and upper boundary of 0.79. Meaning, its taking words that indicate a message is most likely ham, and most
 likely spam, eliminating the 0.5 realm. This results in the accuracy and precision in the screenshot above. 
 As example, if the user adjust the values in the UI to 0.5 for both, the accuracy and precisions when re-calculated can
 be observed to get worse and worse for each re-calculation.<p>
 
 <h2>How-To-Run</h2>
 <p>Make sure javafx is installed on your machine. If not follow this link first: <a href="https://openjfx.io/openjfx-docs/">JavaFX</a></p>
 <ul>
 <li>Download or Clone</li>
 <li>IDE: run the Main.java file</li>
 <li>Terminal: Go to class files directory of project, run <code>--module-path="../../javafx-sdk-11.0.1/lib/" --add-modules="javafx.web" --add-modules="javafx.controls" --add-modules="javafx.fxml" --add-modules="javafx.base -java Main.class"</code>
 in the terminal.</li>
  </ul>
  
<h2>References</h2>
<p>
<a href="https://en.wikipedia.org/wiki/Bag-of-words_model">https://en.wikipedia.org/wiki/Bag-of-words_model </a> <br />
<a href="https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering">https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering </a>
</p>