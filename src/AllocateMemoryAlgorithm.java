import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AllocateMemoryAlgorithm {

    public class MemoryBlock {
        private int size;
        private int allocatedSize;

        public MemoryBlock(int size){
            this.size = size;
            this.allocatedSize = 0;
        }

        public MemoryBlock(MemoryBlock other){
            this.size = other.size;
            this.allocatedSize = other.allocatedSize;
        }

        public int getSize(){
            return size;
        }

        public int getFreeSize(){
            return size - allocatedSize;
        }

        public boolean allocate(int size){
            if(size > getFreeSize()) return false;
            allocatedSize += size;
            return true;
        }

        @Override
        public String toString(){
            return "[Size = " + size + "KB | " + allocatedSize + "KB allocated]";
        }
        
    }

    private List<MemoryBlock> blocks;
    private List<Integer> processes;
    private List<String> outputs;

    public AllocateMemoryAlgorithm(){
        blocks = new ArrayList<>();
        processes = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    public List<MemoryBlock> copyListBlocks(){
        List<MemoryBlock> res = new ArrayList<>();
        for (MemoryBlock memoryBlock : blocks) {
            res.add(new MemoryBlock(memoryBlock));
        }
        return res;
    }

    public void enter(){

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of block: ");
        int numberOfBlock = sc.nextInt();
        for(int i = 0; i < numberOfBlock; i++){
            System.out.print("Enter size of block " + (i + 1) + ": ");
            int size = sc.nextInt();
            MemoryBlock block = new MemoryBlock(size);
            blocks.add(block);
        }

        System.out.print("Enter number of process: ");
        int numberOfProcess = sc.nextInt();
        for(int i = 0; i < numberOfProcess; i++){
            System.out.print("Enter size of process " + (i + 1) + ": ");
            int size = sc.nextInt();
            processes.add(size);
        }

        sc.close();

    }

    public void printOutputs(String kindOfAllocate){
        System.out.println("--- " + kindOfAllocate + ":");
        for (String mess : outputs) {
            System.out.println(mess);
        }
        outputs.clear();
    }

    public void allocateFirstFit(){
        List<MemoryBlock> blocksHandle = copyListBlocks();
        for(int i = 0; i < processes.size(); i++){
            int process = processes.get(i);
            for(int j = 0; j < blocksHandle.size(); j++){
                MemoryBlock block = blocksHandle.get(j);
                if(block.getFreeSize() >= process){
                    block.allocate(process);
                    outputs.add(
                        "Process " + (i + 1) + "(" + process + "KB) be located by block " + (j + 1) + " - " + block
                    );
                    break;
                }
            }
            if(outputs.get(i).isEmpty())
                outputs.add(
                    "Process " + (i + 1) + "(" + process + "KB) be not located"
                );
        }
        printOutputs("First-Fit");
    }


    public void allocateBestFit(){
        List<MemoryBlock> blocksHandle = copyListBlocks();
        for(int i = 0; i < processes.size(); i++){
            int process = processes.get(i);
            int index = -1;
            int min_sizeOfBlock = Integer.MAX_VALUE;
            for(int j = 0; j < blocksHandle.size(); j++){
                MemoryBlock block = blocksHandle.get(j);
                if(block.getFreeSize() >= process && block.getFreeSize() < min_sizeOfBlock){
                    min_sizeOfBlock = block.getFreeSize();
                    index = j;
                }
            }
            if(index != -1) {
                blocksHandle.get(index).allocate(process);
                outputs.add(
                    "Process " + (i + 1) + "(" + process + "KB) be located by block " + (index + 1) + " - " + blocksHandle.get(index)
                );
            } else outputs.add(
                        "Process " + (i + 1) + "(" + process + "KB) be not located"
                    );
        }
        printOutputs("Best-Fit");
    }

    public void allocateWorstFit(){
        List<MemoryBlock> blocksHandle = copyListBlocks();
        for(int i = 0; i < processes.size(); i++){
            int process = processes.get(i);
            int index = -1; 
            int max_sizeOfBlock = Integer.MIN_VALUE;
            for(int j = 0; j < blocksHandle.size(); j++){
                MemoryBlock block = blocksHandle.get(j);
                if(block.getFreeSize() >= process && block.getFreeSize() > max_sizeOfBlock){
                    max_sizeOfBlock = block.getFreeSize();
                    index = j;
                }
            }
            if(index != -1) {
                blocksHandle.get(index).allocate(process);
                outputs.add(
                    "Process " + (i + 1) + "(" + process + "KB) be located by block " + (index + 1) + " - " + blocksHandle.get(index)
                );
            } else outputs.add(
                        "Process " + (i + 1) + "(" + process + "KB) be not located"
                    );
        }
        printOutputs("Worst-Fit");
    }


    public static void main(String[] args) {
        AllocateMemoryAlgorithm allocator = new AllocateMemoryAlgorithm();
        allocator.enter();

        allocator.allocateFirstFit();
        allocator.allocateBestFit();
        allocator.allocateWorstFit();
   
    }

}
