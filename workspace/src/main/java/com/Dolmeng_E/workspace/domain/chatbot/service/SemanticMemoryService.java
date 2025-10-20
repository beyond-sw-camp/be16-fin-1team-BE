package com.Dolmeng_E.workspace.domain.chatbot.service;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import redis.clients.jedis.Jedis;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Component
public class SemanticMemoryService {

    private final WebClient webClient;
    private final String apiKey;
    private final Jedis jedis;

    public SemanticMemoryService(
            @Value("${openai.api.key}") String apiKey,
            @Value("${spring.redis-stack.host}") String redisHost,
            @Value("${spring.redis-stack.port}") int redisPort
    ) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.jedis = new Jedis(redisHost, redisPort);
    }

    // --- 1. OpenAI 임베딩 생성 ---
    public float[] createEmbedding(String text) {
        EmbReq req = new EmbReq("text-embedding-3-small", text);

        EmbRes res = webClient.post()
                .uri("/embeddings")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(EmbRes.class)
                .block();

        List<Double> list = res.getData().get(0).getEmbedding();
        float[] vec = new float[list.size()];
        for (int i = 0; i < list.size(); i++) vec[i] = list.get(i).floatValue();
        return vec;
    }

    // --- 2. Redis 저장 ---
    public void saveToRedis(String userId, String workspaceId, String role, String text) {
        float[] embedding = createEmbedding(text);
        byte[] binary = floatArrayToBytes(embedding);

        String key = "chatmem:" + UUID.randomUUID();

        Map<byte[], byte[]> map = new HashMap<>();
        map.put("userId".getBytes(), userId.getBytes());
        map.put("workspaceId".getBytes(), workspaceId.getBytes());
        map.put("role".getBytes(), role.getBytes());
        map.put("text".getBytes(StandardCharsets.UTF_8), text.getBytes(StandardCharsets.UTF_8));
        map.put("ts".getBytes(), String.valueOf(Instant.now().toEpochMilli()).getBytes());
        map.put("embedding".getBytes(), binary);

        // Hash 저장
        jedis.hset(key.getBytes(), map);

        // TTL 30분 (1800초)
        jedis.expire(key, 1800);
    }

    // --- 3. 유사 문장 검색 (KNN) ---
    public List<String> searchSimilarText(String query, int k) {
        float[] embedding = createEmbedding(query);
        byte[] binary = floatArrayToBytes(embedding);

        // Redis CLI처럼 직접 명령 문자열 전송
        Object result = jedis.sendCommand(
                new redis.clients.jedis.commands.ProtocolCommand() {
                    @Override
                    public byte[] getRaw() {
                        return "FT.SEARCH".getBytes();
                    }
                },
                "chatmem_idx".getBytes(),
                ("*=>[KNN " + k + " @embedding $vec AS score]").getBytes(),
                "PARAMS".getBytes(), "2".getBytes(),
                "vec".getBytes(), binary,
                "SORTBY".getBytes(), "score".getBytes(),
                "DIALECT".getBytes(), "2".getBytes(),
                "RETURN".getBytes(), "2".getBytes(),
                "text".getBytes(), "score".getBytes()
        );

        System.out.println("eunsung Redis result = " + result);
        printRedisResult(result);
        return List.of(String.valueOf(result));
    }

    public void printRedisResult(Object result) {
        if (result instanceof List<?> list) {
            for (Object item : list) {
                printRedisResult(item); // 재귀적으로 내부 리스트 탐색
            }
        } else if (result instanceof byte[] bytes) {
            System.out.println("🧩 " + new String(bytes, StandardCharsets.UTF_8));
        } else {
            System.out.println(result);
        }
    }



    // --- 유틸 ---
    private static byte[] floatArrayToBytes(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocate(arr.length * 4).order(ByteOrder.LITTLE_ENDIAN);
        for (float v : arr) bb.putFloat(v);
        return bb.array();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // --- 내부 DTO ---
    public record EmbReq(String model, String input) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmbData {
        private List<Double> embedding;
        public List<Double> getEmbedding() { return embedding; }
        public void setEmbedding(List<Double> embedding) { this.embedding = embedding; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmbRes {
        private List<EmbData> data;
        public List<EmbData> getData() { return data; }
        public void setData(List<EmbData> data) { this.data = data; }
    }
}

