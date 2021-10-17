# NaiveBayesLearning
Given a training dataset, prints to stdout the parameters of the classifier that were estimated from the training data (i.e., the class priors and the class-conditional probabilities), Then records the accuracy of predictions given test dataset.

Jason John 
JJJ170000
CS 4375 HW1 Part 2

## platform
Java

## purpose
The program shall read the training data given and record all probabilities of each probability within the naive bayes algorithm. For example P(a1,a2|c) -> P(a1|c1), P(a2|c1), P(a1|c2), P(a2,c2) recorded.
The program will then make predictions on the the class instance from the test dataset and print the accuracy of estimates.

## what to work on
Currently, issues with accuracy being somewhat lower to expected goal

## instructions on use
For running in the terminal, you will need to first type the command "javac CS4375A2.java"

Then you will need to type the command "java CS4375A2"

The console will prompt the user for the file name of the training data, and once entered, will then prompt for the file name of the testing data. Example data files are also added to the repository.
