package company.vk.edu.distrib.compute.netheer.kv;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReplicaSelector {
    private final int[] ports;
    private final int replicationFactor;

    private record Candidate(int port, long score) {}

    public ReplicaSelector(int[] ports, int replicationFactor) {
        if (replicationFactor < 1 || replicationFactor > ports.length) {
            throw new IllegalArgumentException("There are less replicas than the request requires");
        }

        this.ports = ports.clone();
        if (ports.length == 0) {
            throw new IllegalArgumentException("There are no ports in request");
        }
        Set<Integer> seen = new HashSet<>();
        for (int port : ports) {
            if (!seen.add(port)) {
                throw new IllegalArgumentException("There are 2 or more ports with the same number");
            }
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("There is a port with illegal number " + port);
            }
        }
        this.replicationFactor = replicationFactor;
    }

    public List<Integer> selectReplicas(String key) {
        List<Candidate> candidates = new ArrayList<>();

        for (int port : ports) {
            long score = score(key, port);
            Candidate candidate = new Candidate(port, score);
            candidates.add(candidate);
        }
        candidates.sort((left, right) -> {
           int comparison = Long.compareUnsigned(right.score(), left.score());
           if (comparison != 0) {
               return comparison;
           }

           return Integer.compare(left.port(), right.port());
        });

        List<Integer> result = new ArrayList<>(replicationFactor);
        for (int i = 0; i < replicationFactor; ++i) {
            result.add(candidates.get(i).port());
        }

        return result;
    }

    private static long score(String key, int port) {
        byte[] input = (key + ":" + port).getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input);
            return ByteBuffer.wrap(hash).getLong();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 using failed", e);
        }
    }

    public int replicationFactor() {
        return replicationFactor;
    }
}
