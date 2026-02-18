package psp.block;
//Ainoha Yubero Timón

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class Block {

    private final int index; // posicion
    private final String hash;
    private final String previousHash;
    private final String dataHash;    // Aquí irá el hash del registro SQL
    private final long timeStamp;

    public Block(int index, String dataHash, String previousHash) {
        this.index = index;
        this.dataHash = dataHash;
        this.previousHash = previousHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        // SHA256(index + timestamp + data + previousHash)
        String input = index + Long.toString(timeStamp) + dataHash + previousHash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getHash() { return hash; }
    public String getPreviousHash() { return previousHash; }
    public String getDataHash() { return dataHash; }
    public int getIndex() { return index; }
}
