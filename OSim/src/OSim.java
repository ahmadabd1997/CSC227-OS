import java.io.*;
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
	
	public static PCB[] readFile(String lo){
		PCB[] pcb= new PCB[5000]; //Size can vary
		String line="";
		int count=0; 
		 try {
	            FileReader fileReader = new FileReader(lo);
	            BufferedReader bufferedReader = new BufferedReader(fileReader);

	            while((line = bufferedReader.readLine()) != null) {  	//go through the file line by line
	                int cp,sz,id,size,time;
	                cp = line.indexOf(";CPU"); 							//flag where ID ends
	                sz = line.indexOf(";SZ");							//flag where CPU time ends
	                id = Integer.parseInt(line.substring(3, cp));  		//Read ID then Convert it from String to integer
	                time = Integer.parseInt(line.substring(cp+5, sz));	//Read CPU time then Convert it from String to integer
	                size = Integer.parseInt(line.substring(sz+4));		//Read the Size then Convert it from String to integer
	                pcb[count++]= new PCB(id,size,time,PCB.state.New);	// Add everything to PCB[i] Array 
	            }   
	            System.out.println(count+" Processes added");

	            // close file.
	            bufferedReader.close();         
	        }
	        catch(FileNotFoundException ex) { 
	            System.out.println("Unable to open file '" +lo + ","+ex);                
	        }
	        catch(IOException ex) { 
	            System.out.println("Error reading file '"+ lo + ","+ex);                  
	        }catch(Exception ex) {  // Every other error
	            System.out.println("Error '"+ lo + ","+ex);                  
		        }
		 return pcb;
	}
	
	public static void writeFile(String filename,PCB[] pcb,int size){
		int i = 0;
        try {
            FileWriter fileWriter = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            while(i<size){ //go through the PCBs array
	            bufferedWriter.write("ID:"+pcb[i].getId()+";CPU:"+pcb[i].getTime()+";SZ:"+pcb[i].getSize()); // Write it in correct formating
	            bufferedWriter.newLine();
	            i++;
            }
            // close file.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '"+ filename + ","+ex);

        }catch(Exception ex) {  // Every other error
            System.out.println("Error '"+ filename + ","+ex);                  
	        }
	}
}
