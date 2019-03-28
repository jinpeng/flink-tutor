# flink-tutor
Apache Flink tutorial code. 
Using the following techniques:
- Apache Flink 1.7.2 (Scala 2.11)
- Gradle 5.3
- Gradle Kotlin DSL

Flink features:
- Load dataset from CSV files
- Filter dataset
- GroupBy and reduce
- Partition to sort
- Passing parameters by command line arguments

Gradle Kotlin DSL features:
- Java application plugins
- Java source and target comapatibility
- Respositories of Aliyun for Chinese developers
- Task for run multiple Java applications besides default run
- Passing arguments to Java applications

Run:
```
$ gradle clean build
$ gradle run --args="--input ../ml-latest-small/movies.csv --output filter-output"
$ gradle averageRating --args="--movies ../ml-latest-small/movies.csv --ratings ../ml-latest-small/ratings.csv"
```

Datasets:
Movielens data downloaded from:
https://grouplens.org/datasets/movielens/

