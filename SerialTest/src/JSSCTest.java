import jssc.SerialPort;
import jssc.SerialPortList;

public class JSSCTest {
	public static void main(String[] args) throws Exception{
		String[] portNames = SerialPortList.getPortNames();
		for (int i = 0; i < portNames.length; i++) {
			System.out.println(portNames[i]);
		}
	
		SerialPort serialPort = new SerialPort("COM5");
		serialPort.openPort();//Open serial port
		serialPort.setParams(SerialPort.BAUDRATE_115200,
		                   SerialPort.DATABITS_8,
		                   SerialPort.STOPBITS_1,
		                   SerialPort.PARITY_NONE);
		new ReadThread(serialPort).start();
		new WriteThread(serialPort).start();
	}
	
	
}


class ReadThread extends Thread {
	SerialPort serial;
	ReadThread(SerialPort serial){
		this.serial = serial;
	}
	
	public void run() {
		try {
			while (true) {
				byte[] read = serial.readBytes();
				if(read != null && read.length > 0) System.out.println(new String(read));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class WriteThread extends Thread{
	SerialPort serial;
	
	WriteThread(SerialPort serial){
		this.serial = serial;
	}
	
	public void run() {
		try {
			int c = 0;
			
			System.out.println("\nKeyborad Input Read!!!!");
			while ((c = System.in.read()) > -1) {
				serial.writeInt(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}