/**
 * @date 2020-08-31
 * 異쒖쿂 : https://searchmawang.tistory.com/entry/RXTX%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-Serial-%EC%86%A1%EC%88%98%EC%8B%A0-%EC%98%88%EC%A0%9C
 */
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Properties;


public class TwoWaySerialComm
{
	/** 기본 접속할 시리얼 포트 */
	final static String DEFAULT_COM_PORT = "COM5";
	/** 기본 baudrate 값 : 115200 */
	final static int DEFAULT_BAUDRATE = 115200;

    public TwoWaySerialComm()
    {
        super();
    }
    
    
    /** 지정한 시리얼 포트와 접속을 시도합니다.
     * @param portName 시리얼 포트 이름. ex: "COM5", "/dev/serial0"
     */
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams( 115200,
                               SerialPort.DATABITS_8,
                               SerialPort.STOPBITS_1,
                               SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    /** */
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    System.out.print(new String(buffer,0,len));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    public static void main ( String[] args ) {
        try {
        	Properties prop = new Properties();
            
        	String strConfigFileName = System.getProperty("user.dir") + "/config/agent.ini";
        	String strComPort = DEFAULT_COM_PORT;
        	int nBaudRate = 115200;
        	String strLogLevel = "debug";
        	String strLogFolder = "./log";
        	File file = new File(strConfigFileName);
        	if (file.exists()) {
	            prop.load(new FileInputStream(strConfigFileName));
	            
	            strComPort = prop.getProperty("COM_PORT", DEFAULT_COM_PORT);
	            try { nBaudRate = Integer.parseInt(prop.getProperty("BAUDRATE"), DEFAULT_BAUDRATE); } catch (Exception e) {}
	            strLogLevel = prop.getProperty("LOG_LEVEL");
	            strLogFolder = prop.getProperty("LOG_FOLDER");

	            System.out.println("COM_PORT = " + strComPort);
	            System.out.println("BAUDRATE = " + nBaudRate);
	            System.out.println("LOG_LEVEL = " + strLogLevel);
	            System.out.println("LOG_FOLDER = " + strLogFolder);
        	}

            (new TwoWaySerialComm()).connect(strComPort);
        }
        catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}