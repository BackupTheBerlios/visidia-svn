package visidia.simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectWriter {
    
    private File file;
    private FileOutputStream fileOS ;
    private ObjectOutputStream objectOS;

    public ObjectWriter (){
    }

    
    public synchronized void open(File file_) {
	this.file = file_;
	
	try {
	    this.fileOS = new FileOutputStream(this.file);
	    this.objectOS = new ObjectOutputStream(this.fileOS);
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
	    this.objectOS.close();
	    this.fileOS.close();
	}
	catch (IOException e) {
	}
    }

    public synchronized void writeObject (Object o) {
	try {
	    this.objectOS.writeObject(o);
	    this.objectOS.flush();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
