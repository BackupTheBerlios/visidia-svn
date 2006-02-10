package visidia.simulation;

import java.io.*;

public class ObjectWriter {
    
    private File file;
    private FileOutputStream fileOS ;
    private ObjectOutputStream objectOS;

    public ObjectWriter (){
    }

    
    public synchronized void open(File file_) {
	file = file_;
	
	try {
	    fileOS = new FileOutputStream(file);
	    objectOS = new ObjectOutputStream(fileOS);
	}
	catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public synchronized void close() {
	try {
	    objectOS.close();
	    fileOS.close();
	}
	catch (IOException e) {
	}
    }

    public synchronized void writeObject (Object o) {
	try {
	    objectOS.writeObject(o);
	    objectOS.flush();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
