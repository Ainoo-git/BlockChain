package psp;

import java.util.ArrayList;
import java.util.List;

public class BlockchainManager {

    private final List<Block> blockchain = new ArrayList<>();

    public BlockchainManager() {
        blockchain.add(new Block(0, "GENESIS", "0"));
    }

    public synchronized void addBlock(String data) {
        Block previous = blockchain.getLast();
        Block nuevo = new Block(blockchain.size(), data, previous.hash);
        blockchain.add(nuevo);
    }

    public boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block actual = blockchain.get(i);
            Block anterior = blockchain.get(i - 1);

            Block recalculado = new Block(
                    actual.index,
                    actual.data,
                    actual.previousHash
            );

            if (!actual.hash.equals(recalculado.hash)) {
                return false;
            }

            if (!actual.previousHash.equals(anterior.hash)) {
                return false;
            }
        }
        return true;
    }
}
