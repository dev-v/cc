package com.cc.report.thread;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;

public class RunPipe extends Thread{
	public boolean creating=true;
	private PipedInputStream in;
	private OutputStream out;
	public RunPipe(PipedInputStream in,OutputStream out){
		this.in=in;
		this.out=out;
	}
	public void run() {
		int i=0;
		while(creating){
			try {
				System.out.println(++i);
				byte[] temp=new byte[1024*10];
				while(in.read(temp)!=-1){
					out.write(temp);
				}
				System.out.println(creating);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
