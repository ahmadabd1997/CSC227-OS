
public class PCB {
	
	public enum state{
		New,Ready,Running,Waiting,Terminated
	}
	
	private int size;
	private int id;
	private state state;
	private int time;
	
	public PCB(int id,int size,int time,state state){
		this.id = id;
		this.size = size;
		this.time = time;
		this.state = state;
	}
	
	public PCB(PCB p){
		this.id = p.id;
		this.size = p.size;
		this.time = p.time;
		this.state = p.state;
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
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}

	public state getState() {
		return state;
	}

	public void setState(state state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String s = "PCB #"+id+" st : " + state + ", size : " + size +", time : " + time;
		return s;
	}
	
	public int compareTime(PCB x){ // (-)if smaller  (+)if bigger  (0) equal 
		return this.time - x.time;
	}
	public int compareSize(PCB x){// (-)if smaller  (+)if bigger  (0) equal
		return this.size - x.size;
	}
}
