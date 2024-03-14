import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AllocateMemory {

    private List<MemoryBlock> blocks; //danh sách lưu trữ thông tin của các phân vùng (memory block)
    private List<Integer> processes; //danh sách lưu trữ độ lớn của các tiến trình (process)
    private List<String> messages; 

    public class MemoryBlock {
        private int size; //kích thước của memory block
        private int remainingSize; //kích thước còn lại

        public MemoryBlock(int size){
            this.size = size;
            this.remainingSize = size;
        }

        public MemoryBlock(MemoryBlock other){
            this.size = other.size;
            this.remainingSize = other.remainingSize;
        }

        public int getSize(){
            return size;
        }

        public int getFreeSize(){
            return remainingSize;
        }

        public int getAllocatedSize(){
            return size - remainingSize;
        }

        public boolean allocate(int size){
            if(size > getFreeSize()) return false;
            remainingSize -= size;
            return true;
        }

        @Override
        public String toString(){
            return " --> Size = " + size + "KB - " + getAllocatedSize() + "KB allocated => " + remainingSize + "KB Free Size";
        }
        
    }

    public AllocateMemory(){
        blocks = new ArrayList<>();
        processes = new ArrayList<>();
        messages = new ArrayList<>();
    }

    //hàm copy List Memory Block ra để test - tránh mất dữ liệu
    public List<MemoryBlock> copyListBlocks(){
        List<MemoryBlock> result = new ArrayList<>();
        for (MemoryBlock memoryBlock : blocks) {
            result.add(new MemoryBlock(memoryBlock));
        }
        return result;
    }

    //hàm nhập dữ liệu cho memory blocks và processes
    public void enter(){

        Scanner sc = new Scanner(System.in);

        System.out.print("Number of blocks: ");
        int n = sc.nextInt();

        for(int i = 0; i < n; i++){
            System.out.print("- Size(KB) of block " + (i + 1) + ": ");
            int size = sc.nextInt();
            MemoryBlock block = new MemoryBlock(size);
            blocks.add(block);
        }

        System.out.print("Number of processes: ");
        int m = sc.nextInt();

        for(int i = 0; i < m; i++){
            System.out.print("- Size(KB) of process " + (i + 1) + ": ");
            int size = sc.nextInt();
            processes.add(size);
        }

        sc.close();
    }

    //hàm thực hiện giải thuật First-Fit
    public void allocateFirstFit(){
        List<MemoryBlock> blocksTmp = copyListBlocks();

        for(int i = 0; i < processes.size(); i++){
            int process = processes.get(i);

            for(int j = 0; j < blocksTmp.size(); j++){
                MemoryBlock block = blocksTmp.get(j);
                if(block.getFreeSize() >= process){
                    block.allocate(process);
                    messages.add(
                        "Block " + (j + 1) + " allocated to process " + (i + 1) + "(" + process + "KB)" + block
                    );
                    break;
                }
            }

            if(messages.get(i).isEmpty())
                messages.add(
                    "Process " + (i + 1) + "(" + process + "KB) be not located"
                );
        }
    }

    //hàm thực hiện giải thuật Best-Fit
    public void allocateBestFit(){
        List<MemoryBlock> blocksTmp = copyListBlocks();

        for(int i = 0; i < processes.size(); i++){
            int process = processes.get(i);
            int index = -1;
            int min_size = Integer.MAX_VALUE;

            for(int j = 0; j < blocksTmp.size(); j++){
                if(blocksTmp.get(j).getFreeSize() >= process && blocksTmp.get(j).getFreeSize() < min_size){
                    min_size = blocksTmp.get(j).getFreeSize();
                    index = j;
                }
            }

            if(index != -1) {
                blocksTmp.get(index).allocate(process);
                messages.add(
                    "Block " + (index + 1) + " allocated to process " + (i + 1) + "(" + process + "KB)" + blocksTmp.get(index)
                );
            } else  messages.add(
                        "Process " + (i + 1) + "(" + process + "KB) be not located"
                    );
        }
    }

    //hàm thực hiện giải thuật Worst-Fit
    public void allocateWorstFit(){
        List<MemoryBlock> blocksTmp = copyListBlocks();

        for(int i = 0; i < processes.size(); i++){
            int process = processes.get(i);
            int index = -1;
            int max_size = Integer.MIN_VALUE;

            for(int j = 0; j < blocksTmp.size(); j++){
                if(blocksTmp.get(j).getFreeSize() >= process && blocksTmp.get(j).getFreeSize() > max_size){
                    max_size = blocksTmp.get(j).getFreeSize();
                    index = j;
                }
            }

            if(index != -1) {
                blocksTmp.get(index).allocate(process);
                messages.add(
                    "Block " + (index + 1) + " allocated to process " + (i + 1) + "(" + process + "KB)" + blocksTmp.get(index)
                );
            } else  messages.add(
                        "Process " + (i + 1) + "(" + process + "KB) be not located"
                    );
        }
    }

    //hàm hiển thị thông tin kết quả của các giải thuật - tham số: tên loại giải thuật
    public void printMessages(String kindOfMess){
        System.out.println("\n========== " + kindOfMess + " ==========");
        for (String mess : messages) {
            System.out.println(mess);
        }
        messages.clear();
    }

    public static void main(String[] args) {
        
        //tạo đối tượng lưu trữ dữ liệu và nhập dữ liệu
        AllocateMemory dataObj = new AllocateMemory();
        dataObj.enter();

        //thực hiện các giải thuật và hiển thị kết quả
        dataObj.allocateFirstFit();
        dataObj.printMessages("First-Fit");

        dataObj.allocateBestFit();
        dataObj.printMessages("Best-Fit");

        dataObj.allocateWorstFit();
        dataObj.printMessages("Worst-Fit");
   
    }
}
