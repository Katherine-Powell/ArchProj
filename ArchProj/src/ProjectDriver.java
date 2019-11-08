import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class ProjectDriver {
	
	public static void main(String[] args) throws FileNotFoundException {
		String traceFileName = new String(args[2]);
		int i;
		
		File inputFile = new File (traceFileName);
		Scanner scan = new Scanner(inputFile);
			
		PrintStream outputFile = new PrintStream(new File("Trace4.trc"));
				
		// Initialize Command Line Arguments
		Cache cache = new Cache(args);
		
		System.setOut(outputFile);
		System.out.println("Cache Simulator CS 3853 Fall 2019 - Group #4\n");
		
		// Print command line arguments
		System.out.print("Cmd Line: ");
		for(i = 0; i < args.length; i++) {
			System.out.print(args[i] + " ");
		}
		
		System.out.println("\nTrace File: " + traceFileName);
		System.out.println("Cache Size: " + Util.convertToKB(cache.getCacheSize()) + " KB");
		System.out.println("Block Size: " + cache.getBlockSize() + " bytes");
		System.out.println("Associativity: " + cache.getAssocName());
		System.out.println("R-Policy: " + cache.getRPolicy());
		
		System.out.println("\nGeneric: ");
		System.out.println("Cache Size: " + Util.convertToKB(cache.getCacheSize()) + " KB");
		System.out.println("Block Size: " + cache.getBlockSize() + " bytes");
		System.out.println("Associativity: " + cache.getAssociativityNum());
		System.out.println("Policy: " + cache.getRPolicy());
		
		System.out.println("----- Calculated Values -----");
		System.out.println("Total #Blocks: " + Util.convertToKB(cache.getNumBlocks()) + " KB " + "(2^ " + Util.logBase2(cache.getNumBlocks()) + ")");
		System.out.println("Tag Size: " + cache.getTagBits() + " bits");
		System.out.println("Index Size: " + cache.getIndexBits() + " bits, Total Indices: " + Util.convertToKB(cache.getNumIndicies()) + " KB");
		System.out.println("Overhead Memory Size: " + cache.getOverhead() + " bytes (or " + Util.convertToKB(cache.getOverhead()) + " KB)");
		System.out.println("Implementation Memory Size: " + cache.getImpMemorySize() + " bytes (or " + Util.convertToKB(cache.getImpMemorySize()) + " KB)");
		
		System.out.println("----- Results -----");
		System.out.println("Cache Hit Rate: ***%");
		System.out.println("CPI: \n");		
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			if((line.contains("EIP")) || (line.contains("dstM"))) {
				parseAddressOne(line);
				line = scan.nextLine();
				parseAddressTwo(line);
				parseAddressThree(line);
			}
		}
		
	}
	
	public static int parseAddressOne (String line) {
		char[] lineArray = line.toCharArray();
		String grabAddress = new String(); 
		String length = new String();
		int i;
		
		for (i=10; i<18; i++) {
			grabAddress += lineArray[i];
		}
		
		for (i=5; i<7; i++) {
			length += lineArray[i];
		}
		
		int address = Integer.parseInt(grabAddress, 16);
		return address;
		//String.format("0x%08x: (%d)", address, Integer.parseInt(length));
	}
	
	public static int parseAddressTwo (String line) {
		char[] lineArray = line.toCharArray();
		String dstM = new String();
		int i, dstAddress;
		
		for (i=6; i<14; i++) {
			dstM += lineArray[i];
		}	
		
		dstAddress = Integer.parseInt(dstM, 16);
		
		return dstAddress;
	}
	
	public static int parseAddressThree (String line) {
		char[] lineArray = line.toCharArray();
		String srcM = new String();
		int i, srcAddress;
		
		for (i=33; i<41; i++) {
			srcM += lineArray[i];
		}
		
		srcAddress = Integer.parseInt(srcM, 16);
		return srcAddress;
	}
}
