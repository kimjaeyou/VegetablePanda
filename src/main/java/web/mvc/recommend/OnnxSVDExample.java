//package web.mvc.recommend;
//
//import ai.onnxruntime.*;
//
//import java.util.*;
//import java.nio.FloatBuffer;
//
//public class OnnxSVDExample {
//
//    public static void main(String[] args) throws Exception {
//        // ONNX 모델 경로
//        String modelPath = "C:/Users/nonam/Desktop/kostaPro/PyLearnData/svd_model_v9.onnx";
//
//        // ONNX 환경 및 세션 생성
//        OrtEnvironment env = OrtEnvironment.getEnvironment();
//        OrtSession.SessionOptions options = new OrtSession.SessionOptions();
//        OrtSession session = env.createSession(modelPath, options);
//
//        // 유저-아이템 데이터 예제 (Python과 동일한 형태로 제공)
//        float[][] input = {
//                {5.0f, 4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 3.0f, 5.0f, 3.0f, 5.0f, 3.0f, 5.0f,
//                        5.0f, 4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 3.0f, 5.0f}
//        };
//
//        Map<String, OnnxTensor> inputs = new HashMap<>();
//        inputs.put("float_input", OnnxTensor.createTensor(env, input));
//
//        // ONNX 모델 실행
//        OrtSession.Result result = session.run(inputs);
//
//        // 결과 추출
//        // 예측된 값
//        float[][] output = (float[][]) result.get(0).getValue();
//
//        // 유저-아이템 행렬의 아이템 ID (예: review_seq)
//        int[] itemIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};  // 예시로 10개의 아이템 ID
//
//        // 추천 시스템: 예측된 값 기반으로 상품 추천
//        // 예측 점수를 기준으로 상위 3개 아이템 추천
//        Map<Integer, Float> itemScores = new HashMap<>();
//        for (int i = 0; i < output[0].length; i++) {
//            itemScores.put(itemIds[i], output[0][i]); // 아이템 ID와 예측 점수 매핑
//        }
//
//        // 예측 점수를 기준으로 내림차순 정렬하여 추천
//        List<Map.Entry<Integer, Float>> sortedItems = new ArrayList<>(itemScores.entrySet());
//        sortedItems.sort((entry1, entry2) -> Float.compare(entry2.getValue(), entry1.getValue()));
//
//        // 상위 3개 아이템 출력
//        System.out.println("Top 3 Recommended Items:");
//        for (int i = 0; i < 3 && i < sortedItems.size(); i++) {
//            System.out.println("Item ID: " + sortedItems.get(i).getKey() + ", Predicted Score: " + sortedItems.get(i).getValue());
//        }
//
//        // 자원 해제
//        session.close();
//        env.close();
//    }
//}
