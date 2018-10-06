package com.dongjinpeng.movielens;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AverageRating {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String input_movies = parameterTool.getRequired("movies");
        String input_ratings = parameterTool.getRequired("ratings");

        DataSet<Tuple3<Long, String, String>> movies = env.readCsvFile(input_movies)
                .ignoreFirstLine()
                .parseQuotedStrings('"')
                .ignoreInvalidLines()
                .types(Long.class, String.class, String.class);

        DataSet<Tuple2<Long, Double>> ratings = env.readCsvFile(input_ratings)
                .ignoreFirstLine()
                .includeFields(false, true, true, false)
                .types(Long.class, Double.class);

        List<Tuple2<String, Double>> distribution = movies.join(ratings)
                .where(0)
                .equalTo(0)
                .with(new JoinFunction<Tuple3<Long, String, String>, Tuple2<Long, Double>, Tuple3<String, String, Double>>() {
                    @Override
                    public Tuple3<String, String, Double> join(Tuple3<Long, String, String> movie, Tuple2<Long, Double> rating) throws Exception {
                        String name = movie.f1;
                        String genre = movie.f2.split("\\|")[0];
                        Double score = rating.f1;
                        return new Tuple3<>(name, genre, score);
                    }
                })
                .groupBy(1)
                .reduceGroup(new GroupReduceFunction<Tuple3<String, String, Double>, Tuple2<String, Double>>() {
                    @Override
                    public void reduce(Iterable<Tuple3<String, String, Double>> iterable, Collector<Tuple2<String, Double>> collector) throws Exception {
                        String genre = null;
                        int count = 0;
                        double totalScore = 0;
                        for (Tuple3<String, String, Double> movie : iterable) {
                            genre = movie.f1;
                            totalScore += movie.f2;
                            count ++;
                        }
                        collector.collect(new Tuple2<>(genre, totalScore / count));
                    }
                })
                .collect();

        String result = distribution.stream()
                .sorted((r1, r2) -> Double.compare(r1.f1, r2.f1))
                .map(Objects::toString)
                .collect(Collectors.joining("\n"));

        System.out.println(result);
    }
}
