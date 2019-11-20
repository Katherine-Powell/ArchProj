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
			
		PrintStream outputFile = new PrintStream(new File("Trace5.trc"));
				
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
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			if((line.contains("EIP")) || (line.contains("dstM"))) {
				int length = parseLength(line);
				if(cache.getRPolicy().equals("RR")) { //round robin
					cache.addRoundRobinEntry(parseAddressOne(line), length);
					line = scan.nextLine();
					
					//Add 2 clockCycles for data accesses 
					long address2 = parseAddressTwo(line);
					long address3 = parseAddressThree(line);
					if(address2 != 0 || address3 != 0)
						cache.setClockCycles(cache.getClockCycles() + 2);
					
					cache.addRoundRobinEntry(address2 , 4);
					cache.addRoundRobinEntry(address3 , 4);
					
					cache.setInstructionCount(cache.getInstructionCount()+1);
					
					//add 2 clock cycles for every instruction
					cache.setClockCycles(cache.getClockCycles() + 2);
				}
				else if(cache.getRPolicy().equals("RND")) { //random replace
					cache.addRandomEntry(parseAddressOne(line), length);
					line = scan.nextLine();
					cache.addRandomEntry(parseAddressTwo(line) , 4);
					cache.addRandomEntry(parseAddressThree(line), 4);
					
					//Add 2 clockCycles for data accesses 
					long address2 = parseAddressTwo(line);
					long address3 = parseAddressThree(line);
					if(address2 != 0 || address3 != 0)
						cache.setClockCycles(cache.getClockCycles() + 2);
					
					cache.setInstructionCount(cache.getInstructionCount()+1);
					
					//add 2 clock cycles for every instruction
					cache.setClockCycles(cache.getClockCycles() + 2);
				}
			}
		}
		
		
		System.out.println("----- Results -----");
		System.out.printf("Cache Hit Rate: %.1f%%\n",cache.getCacheHitRatio());
		System.out.printf("Cache Miss Rate: %.1f%%\n", 100-cache.getCacheHitRatio());
		System.out.printf("CPI:  %.1f cycles/instruction\n", (double)cache.getClockCycles()/cache.getInstructionCount());	
		
		System.out.println("Hits: " + cache.getNumHits());
		System.out.println("Misses: " + cache.getNumMisses());
		
	}
	
	public static int parseLength(String line) {
		char[] lineArray = line.toCharArray();
		String length = new String();
		
		for (int i=5; i<7; i++) {
			length += lineArray[i];
		}
		
		return Integer.parseInt(length);
	}
	
	public static long parseAddressOne (String line) {
		char[] lineArray = line.toCharArray();
		String grabAddress = new String(); 
		String length = new String();
		int i;
		
		for (i=10; i<18; i++) {
			grabAddress += lineArray[i];
		}
		
		long address = Long.parseLong(grabAddress, 16);
		return address;
	}
	
	public static long parseAddressTwo (String line) {
		char[] lineArray = line.toCharArray();
		String dstM = new String();
		int i;
		long dstAddress;
		
		for (i=6; i<14; i++) {
			dstM += lineArray[i];
		}	
		
		dstAddress = Long.parseLong(dstM, 16);
		
		return dstAddress;
	}
	
	public static long parseAddressThree (String line) {
		char[] lineArray = line.toCharArray();
		String srcM = new String();
		int i;
		long srcAddress;
		
		for (i=33; i<41; i++) {
			srcM += lineArray[i];
		}
		
		srcAddress = Long.parseLong(srcM, 16);
		return srcAddress;
	}
}
