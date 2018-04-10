import java.util.Random;

public class OSim {
	static Random rand;
	static int nPCBs;
	static int nPCBsMM;
	public static void main(String[] args) {
		rand = new Random();
		
		PCB[] HD = new PCB[5000]; // change to Linkedlist(Maybe)
		int HDsize = 2097152; //  should be able to change the size 2097152
		int MMsize = 163840; // 163840
		
		nPCBs = FillHD(HDsize,HD); // Fill the HD
		PCB[] MM = new PCB[3000];
		int SnPCBs = nPCBs;
		arrangeHD(HD);
		nPCBsMM =0;
		Queue<PCB> WQ = new Queue<PCB>();
		CPU CPU = new CPU();
		
		while(nPCBs != 0 || nPCBsMM != 0 || CPU.isBusy()){
			while(nPCBs >0 && MMsize>=HD[nPCBs-1].getSize()){// fill OR add to MM
				
				System.out.println("add HD["+ (nPCBs-1) + "] to MM[" +nPCBsMM+"]");
				
				MMsize -= HD[nPCBs-1].getSize();
				MM[nPCBsMM] = new PCB(HD[--nPCBs]);
				MM[nPCBsMM].setState(PCB.state.Ready);
				nPCBsMM++;
			}
			arrangeMM(MM);
			if(!CPU.isBusy()){ // free (add process to CPU)
				//MMsize += MM[nPCBsMM-1].getSize(); // the process still in memmory (still running) // !!! IMPORTANT !!!
				int i =nPCBsMM-1;
				while(!CPU.isBusy() && i>= 0){
					if(MM[i].getState() == PCB.state.Ready){
						CPU.setProcess(MM[--nPCBsMM]);
						CPU.getProcess().setState(PCB.state.Running);
						CPU.setBusy(true);
					}
					i--;
				}
			}
			else{ // busy (Work) //rand.nextInt()
				CPU.Work();
				if(CPU.getProcess().getTime() == 0)
					CPU.setBusy(false);
				else{
					if(rand.nextInt(100)+1 <= 10){ // interrupt
						PCB temp = CPU.getProcess();
						CPU.setProcess(MM[nPCBsMM-1]);
					}
					else if(rand.nextInt(100)+1 <= 20){ // IO request
						WQ.addFirst(CPU.getProcess());
						CPU.getProcess().setState(PCB.state.Waiting);
					}
				}
			}
			
		}
	}
	
	public static int FillHD(int HDsize,PCB[] HD){ // HDsize in KB
		int nPCBs = 0, size =0, time =0,id =0;
		
		while(HDsize >= 16){
			size = rand.nextInt(16369)+16;
			if(size <= HDsize){
				time = rand.nextInt(101)+100;
				HD[id] = new PCB(id++,size,time,PCB.state.New);
				HDsize -= size;
				nPCBs++;
			}
		}
		
		return nPCBs;
	}
	
	public static void arrangeHD(PCB[] HD){
		PCB temp;
		for(int i=1;i<nPCBs;i++){
			for(int j=0;j<nPCBs-i;j++){
				if(HD[j].compareSize(HD[j+1]) < 0){
					temp = new PCB(HD[j]);
					HD[j] = new PCB(HD[j+1]);
					HD[j+1] = temp;
				}
			}
		}
	}
	
	public static void arrangeMM(PCB[] MM ){
		PCB temp;
		for(int i=1;i<nPCBsMM;i++){
			for(int j=0;j<nPCBsMM-i;j++){
				if(MM[j].compareTime(MM[j+1]) < 0){
					temp = new PCB(MM[j]);
					MM[j] = new PCB(MM[j+1]);
					MM[j+1] = temp;
				}
			}
		}
	}
	
	public static void selectPtoCPU(){
		
	}
}
