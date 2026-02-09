package psp;

import java.security.MessageDigest;

public final class Block {

    public final int index;
    public final long timestamp;
    public final String data;
    public final String previousHash;
    public final String hash;

    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.timestamp = System.currentTimeMillis();
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calcularHash();
    }

    private String calcularHash() {
        try {
            String input = index + timestamp + data + previousHash;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes("UTF-8"));

            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
