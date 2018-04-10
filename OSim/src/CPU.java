
public class CPU {
	private boolean busy;
	private PCB Process;
	
	public CPU(){
		busy = false;
		Process = null;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public PCB getProcess() {
		return Process;
	}

	public void setProcess(PCB process) {
		Process = process;
	}
	
	public void Work(){
		this.Process.setTime(this.Process.getTime()-1);
	}
	
}
