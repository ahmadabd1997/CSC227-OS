import java.io.*;
import java.util.Random;

public class OSim {
	static Random rand;
	static PCB[] HD = new PCB[5000]; // change to Linkedlist(Maybe)
	static PCB[] MM = new PCB[3000];
	static int nPCBs;
	static int nPCBsMM;
	static int HDsize = 200; //  should be able to change the size 2097152
	static int MMsize = 200; // 163840
	
	
	public static void main(String[] args) {
		rand = new Random();
		
		nPCBs = FillHD(); // Fill the HD
		int SnPCBs = nPCBs;
		arrangeHD();
		nPCBsMM =0;
		Queue<PCB> WQ = new Queue<PCB>();
		CPU CPU = new CPU();
		
		while(nPCBs != 0 || nPCBsMM != 0 || CPU.isBusy()){
			//System.out.println("HD processes :");
			
			{
				int i=SnPCBs-1;
				while(i>=0 && nPCBs >0 && MMsize>=HD[i].getSize()){// fill OR add to MM
					if(HD[i].getState() == PCB.state.New){
						System.out.println("add HD["+ (i) + "] = "+HD[i]+" to MM[" +nPCBsMM+"]");
						
						MMsize -= HD[i].getSize();
						MM[nPCBsMM] = HD[i];
						MM[nPCBsMM].setState(PCB.state.Ready);
						nPCBsMM++;
					}
					i--;
				}
			}
			arrangeMM();
			if(!CPU.isBusy()){ // free (add process to CPU)
				//MMsize += MM[nPCBsMM-1].getSize(); // the process still in memmory (still running) // !!! IMPORTANT !!!
				SelectPtoCPU(CPU,MM);
			}
			if(CPU.isBusy()){ // busy (Work) //rand.nextInt()
				CPU.Work();
				if(CPU.getProcess().getCPUrtime() <= 0){ // normal termination
					Terminate(CPU);
				}
				else{
					if(rand.nextInt(100)+1 <= 10){ // Interrupt
						
						PCB temp = CPU.getProcess();
						System.out.println("Interrupted #" + temp.getId());
						temp.setState(PCB.state.Waiting);
						CPU.setBusy(false);
						SelectPtoCPU(CPU,MM);
						temp.setState(PCB.state.Ready);
					}
					else if(rand.nextInt(100)+1 <= 20){ // IO request
						System.out.println("IO request #" + CPU.getProcess().getId());
						CPU.getProcess().setState(PCB.state.Waiting);
						WQ.addLast(CPU.getProcess());
						CPU.setBusy(false);
						SelectPtoCPU(CPU,MM);
					}
					else if(rand.nextInt(100)+1 <= 5){ // Process terminate normally
						System.out.println("Terminate #" + CPU.getProcess().getId());
						Terminate(CPU);
					}
					else if(rand.nextInt(100)+1 <= 1){
						System.out.println("Terminate #" + CPU.getProcess().getId());
						Terminate(CPU);
					}
				}
			}
			if(!WQ.isEmpty()){
				PCB temp = WQ.getFirst();
				temp.IOWork();
				if(temp.getIOrtime() <= 0){ // IO is done
					System.out.println("IO is done #" + temp.getId());
					WQ.removeFirst();
					temp.setState(PCB.state.Ready);
				}
				else if(rand.nextInt(100)+1 <= 20){ // IO terminate
					
					WQ.removeFirst();
					temp.setState(PCB.state.Ready);
					System.out.println("IO terminate " + temp);
					for(int i=0;i<nPCBsMM;i++)
						System.out.println("	MM["+i+"] : " + MM[i]);
				}
			}
			
		}
	}
	
	public static void Terminate(CPU CPU){
		CPU.getProcess().setCPUbound(CPU.getProcess().getCPUctime()>=CPU.getProcess().getIOctime());
		CPU.getProcess().setState(PCB.state.Terminated);
		CPU.setBusy(false);
		CPU.getProcess().setCPUrtime(-5);
		arrangeMM();
		nPCBsMM--;
		MMsize += CPU.getProcess().getSize();
		nPCBs--;
	}
	
	public static boolean SelectPtoCPU(CPU CPU,PCB[] MM){
		int i =nPCBsMM-1;
		CPU.setBusy(false);
		while(!CPU.isBusy() && i>= 0){
			if(MM[i].getState() == PCB.state.Ready){
				System.out.println("Select #" + MM[i].getId());
				CPU.setProcess(MM[i]);
				CPU.getProcess().setState(PCB.state.Running);
				CPU.setBusy(true);
				return true;
			}
			i--;
		}
		return false;
	}
	
	public static int FillHD(){ // HDsize in KB
		int nPCBs = 0, size =0, CPUtime =0, id =0, IOtime =0;
		
		while(HDsize >= 16){
			size = rand.nextInt(200)+16;
			if(size <= HDsize){
				IOtime = rand.nextInt(101)+100;
				CPUtime = rand.nextInt(497)+16;
				HD[id] = new PCB(id++,size,CPUtime,IOtime,PCB.state.New);
				HDsize -= size;
				nPCBs++;
			}
		}
		
		return nPCBs;
	}
	
	public static void arrangeHD(){
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
	
	public static void arrangeMM(){
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

	public static PCB[] readFile(String lo){					//ahmad edited here need to check if correct
		PCB[] pcb= new PCB[5000]; //Size can vary
		String line="";
		int count=0; 
		 try {
	            FileReader fileReader = new FileReader(lo);
	            BufferedReader bufferedReader = new BufferedReader(fileReader);

	            while((line = bufferedReader.readLine()) != null) {  				//go through the file line by line
	                int cp,sz,io,id,size,CPUtime,IOtime;
	                cp = line.indexOf(";CPU"); 										//flag where ID ends
	                sz = line.indexOf(";SZ");										//flag where CPU time ends
	                io = line.indexOf(";IO");										//flag where Size ends
	                id = Integer.parseInt(line.substring(3, cp));  					//Read ID then Convert it from String to integer
	                CPUtime = Integer.parseInt(line.substring(cp+5, sz));			//Read CPU time then Convert it from String to integer
	                size = Integer.parseInt(line.substring(sz+4,io));				//Read the Size then Convert it from String to integer
	                IOtime = Integer.parseInt(line.substring(io+4));
	                pcb[count++]= new PCB(id,size,CPUtime,IOtime,PCB.state.New);	// Add everything to PCB[i] Array 
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
	
	public static void writeFile(String filename,PCB[] pcb,int size){				//ahmad edited here need to check if correct
		int i = 0;
        try {
            FileWriter fileWriter = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            while(i<size){ //go through the PCBs array
	            bufferedWriter.write("ID:"+pcb[i].getId()+";CPU:"+pcb[i].getCPUrtime()+";SZ:"+pcb[i].getSize()+";IO:"+pcb[i].getIObtime()); // Write it in correct formating
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
