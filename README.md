# cs2731-nlp-hw1
In this assignment, you will use language modeling to detect which of three languages a document is written in. 
You will build unigram, bigram, and trigram letter language models (both unsmoothed and smoothed versions) 
for three languages, score a test document with each, and determine the language it is written in based on perplexity. 
You will use the literature to improve your results with a more sophisticated method for adjusting the counts.

I. Main Tasks (same for everyone)
To complete the assignment, you will need to write a program (from scratch) that:

builds the models: reads in a text, collects counts for all letter 1, 2, and 3-grams, estimates probabilities, and writes out the unigram, bigram, and trigram models into files
adjusts the counts: rebuilds the trigram language model using three different methods: LaPlace smoothing, backoff, and linear interpolation with lambdas equally weighted
evaluates all unsmoothed and smoothed models: reads in a test document, applies the language models to all sentences in it, and outputs their perplexity
You may make any additional assumptions and design decisions, but state them in your report (see below).

You may write your program in any TA-approved programming language (so far, java or python).

II. "Research" Task (likely different across the class)
Improve your best-performing model by implementing at least one advanced method compared to the main tasks related to adjusting the counts. Design an experiment that compares the models of Tasks I and II and demonstrates improvement. Your write-up should explain the method and provide a citation from a motivating research paper.

Data
The data for this project is available here. It consists of:
training.en - English training data
training.es - Spanish training data
training.de - German training data
test - test document
