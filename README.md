# Reuter News ETL and Sentiment Pipeline

This project processes Reuters news data using an ETL pipeline, performs word frequency analysis with Apache Spark, and carries out basic sentiment analysis on the news titles using Java.

## Overview

The project is divided into three main tasks:

1. **ETL (Extract, Transform, Load)**
   - Extracts news articles from the `reut2-009.sgm` file.
   - Transforms the data by parsing the titles and body content from the `<TITLE>` and `<BODY>` tags, respectively, within each `<REUTER>` block.
   - Loads the parsed data into a MongoDB database named `ReuterDb`, with each news article stored as a document.
   
2. **Data Processing with Apache Spark**
   - Configures an Apache Spark cluster on Google Cloud Platform (GCP).
   - Reads the `reut2-009.sgm` file directly and performs word frequency analysis.
   - Excludes stop words and generates a frequency count of unique words found in the dataset.
   - Outputs the words with the highest and lowest frequencies.

3. **Basic Sentiment Analysis using Java**
   - Implements a simple bag-of-words model using a Core Java program (without additional libraries).
   - Compares each news title against a predefined list of positive and negative words.
   - Assigns a sentiment label ("positive," "negative," or "neutral") to each title based on the word matches.

## Tasks Breakdown

### 1. ETL Pipeline
- **Input**: Reuters news file `reut2-009.sgm`.
- **Output**: MongoDB Database `ReuterDb` where each document represents a news article.
- **Technology Used**: Java program `ReutReader.java` to scan for `<TITLE>` and `<BODY>` tags and insert data into MongoDB.

### 2. Data Processing with Spark
- **Input**: Raw text from `reut2-009.sgm`.
- **Task**: Perform word frequency count using Apache Spark.
- **Output**: A frequency distribution of unique words, excluding stop words.
- **Technology Used**: Apache Spark cluster on GCP for large-scale data processing.

### 3. Sentiment Analysis
- **Input**: News titles extracted from Reuters data.
- **Task**: Create a bag-of-words for each title, compare with a list of positive/negative words, and assign a sentiment label.
- **Output**: Tagged news titles indicating their sentiment.
- **Technology Used**: Core Java with a regex-based parser for bag-of-words, word-by-word comparison for sentiment tagging.

## How to Run

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- MongoDB installed locally or accessible remotely
- Apache Spark cluster on Google Cloud Platform (GCP)
- Reuters dataset file (`reut2-009.sgm`)

### Steps
1. **ETL**: Run the `ReutReader.java` to parse the news articles and load them into MongoDB.
2. **Data Processing**: Set up a Spark cluster on GCP and run the Spark job to perform word frequency analysis.
3. **Sentiment Analysis**: Execute the Java program that performs bag-of-words creation and sentiment tagging.

### Output
- **MongoDB**: A `ReuterDb` database with documents for each news article.
- **Spark Output**: A frequency count of unique words from the Reuters data.
- **Sentiment Output**: Tagged sentiment ("positive", "negative", "neutral") for each news title.
