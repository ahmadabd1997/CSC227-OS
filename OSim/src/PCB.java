
public class PCB {
	
	public enum state{
		New,Ready,Running,Waiting,Terminated
	}
	
	private int size;
	private int id;
	private state state;
	private int CPUrtime,IOrtime,CPUctime,IOctime,IObtime;
	private boolean CPUbound;
	
	public PCB(int id,int size,int CPUrtime,int IObtime,state state){
		this.id = id;
		this.size = size;
		this.CPUrtime = CPUrtime;
		this.CPUctime = 0;
		this.IOrtime = IObtime;
		this.IOctime = 0;
		this.IObtime = IObtime;
		this.state = state;
		this.CPUbound = false;
	}
	
	public PCB(PCB p){
		this.id = p.id;
		this.size = p.size;
		this.CPUrtime = p.CPUrtime;
		this.CPUctime = p.CPUctime;
		this.IOrtime = p.IOrtime;
		this.IOctime = p.IOctime;
		this.IObtime = p.IObtime;
		this.state = p.state;
		this.CPUbound = p.CPUbound;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	public int getCPUrtime() {
		return CPUrtime;
	}

	public void setCPUrtime(int cPUrtime) {
		CPUrtime = cPUrtime;
	}

	public int getIOrtime() {
		return IOrtime;
	}

	public void setIOrtime(int iOrtime) {
		IOrtime = iOrtime;
	}

	public int getCPUctime() {
		return CPUctime;
	}

	public void setCPUctime(int cPUctime) {
		CPUctime = cPUctime;
	}

	public int getIOctime() {
		return IOctime;
	}

	public void setIOctime(int iOctime) {
		IOctime = iOctime;
	}

	public int getIObtime() {
		return IObtime;
	}

	public void setIObtime(int iObtime) {
		IObtime = iObtime;
	}

	public state getState() {
		return state;
	}

	public void setState(state state) {
		this.state = state;
	}

	public boolean isCPUbound() {
		return CPUbound;
	}

	public void setCPUbound(boolean cPUbound) {
		CPUbound = cPUbound;
	}

	@Override
	public String toString() {
		String s = "PCB #"+id+" st : " + state + ", size : " + size +", Ctime : " + CPUrtime + ", Itime : " + IOrtime;
		return s;
	}
	
	public int compareTime(PCB x){ // (-)if smaller  (+)if bigger  (0) equal 
		return this.CPUrtime - x.CPUrtime;
	}
	public int compareSize(PCB x){// (-)if smaller  (+)if bigger  (0) equal
		return this.size - x.size;
	}
	public void CPUWork(){
		//System.out.println("CPUWORK : " + this);
		CPUrtime-= 1;
		CPUctime+= 1;
	}
	public void IOWork(){
		//System.out.println("IOWORK : " + this);
		IOrtime-= 1;
		IOctime+= 1;
	}
}
