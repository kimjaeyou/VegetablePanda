package web.mvc.recommend;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.SparkSession;

public class SparkRecommender {
    public static void main(String[] args) {
        // Spark 세션을 생성합니다.
        SparkSession spark = SparkSession.builder().appName("Spark Recommender").getOrCreate();
        
        // 사용자-아이템 평점 데이터 (예시: ratings.csv)
        JavaRDD<Rating> ratings = spark.read().textFile("ratings.csv")
                .javaRDD()
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Rating(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2]));
                });

        // JavaRDD를 RDD로 변환
        org.apache.spark.rdd.RDD<Rating> ratingsRDD = ratings.rdd();

        // ALS 모델을 훈련시키기 위한 파라미터 설정
        int rank = 10;  // 잠재적 요소의 개수
        int numIterations = 10;  // 반복 횟수
        double lambda = 0.01;  // 정규화 파라미터
        
        // ALS 모델 훈련
        org.apache.spark.mllib.recommendation.MatrixFactorizationModel model = ALS.train(ratingsRDD, rank, numIterations, lambda);

        // 특정 사용자에게 추천할 아이템을 계산 (예: 사용자 ID가 1인 사용자에게 5개 아이템 추천)
        Rating[] recommendations = model.recommendProducts(1, 5);

        // 추천 결과 출력
        for (Rating recommendation : recommendations) {
            System.out.println("아이템: " + recommendation.product() + ", 추천 평점: " + recommendation.rating());
        }

        // Spark 세션 종료
        spark.stop();
    }
}
