import java.util.ArrayList;

class SimpleBlock {

    long blockID;
    double blockSize = 0;
    ArrayList<SimpleTransaction> txs = new ArrayList<SimpleTransaction>();

    public SimpleBlock(){}

    public SimpleBlock(long blockID, SimpleTransaction tx){
        this();
        this.setID(blockID);
        this.add(tx);
    }

    public void add(SimpleTransaction tx){
        try{
            if (txs.size() >= 500) throw new Exception();   //Cannot store more than 500 txs per block
            txs.add(tx);
            blockSize += tx.size;
        } catch (Exception e) {
            System.out.println("Tried to add too many txs to a block");
        }
    }

    public void setID(long blockID){
        this.blockID = blockID;
    }

}