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
			
		PrintStream outputFile = new PrintStream(new File("Trace1.trc"));
				
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
		
		//TODO: parse input file and print the first 20 addresses and address lengths
		
		
		System.out.println("----- Results -----");
		System.out.println("Cache Hit Rate: ***%");
		System.out.println("CPI: ");
		
		
		
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			if((line.contains("EIP")) || (line.contains("dstM"))) {
			//	scan.nextLine();
				
				System.out.print(parseLineOne(line) + " ");
				System.out.println(parseLineTwo(scan.nextLine()));
			}
		}
		
	}
	
	public static String parseLineOne (String line) {
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
		
		Long hexLong = Long.parseUnsignedLong(grabAddress, 16);
		
		return "Address: " + grabAddress + ", length = " + length + "." + " " + hexLong + " "+Long.toHexString(hexLong);
	}
	
	public static String parseLineTwo (String line) {
		char[] lineArray = line.toCharArray();
		String dstM = new String(); 
		String srcM = new String();
		
		int i, dstMInt, srcMInt;
		
		for (i=6; i<14; i++) {
			dstM += lineArray[i];
		}
		
		for (i=33; i<41; i++) {
			srcM += lineArray[i];
		}
		
	//	dstMInt = Integer.parseInt(dstM);
	//	srcMInt = Integer.parseInt(srcM);
		
		
		if ((dstM.equals("00000000")) && (srcM.equals("00000000"))) {
			return "No data writes/reads occurred.";
		}
		else if (!(dstM.equals("00000000")) && (srcM.equals("00000000"))) {
			return "Data write at " + dstM + ". No data reads occurred, length = 4 bytes.";
		}
		else if ((dstM.equals("00000000")) && !(srcM.equals("00000000"))) {
			return "No data writes occurred. Data read at " + srcM + ", length = 4 bytes.";
		}
		else {
			return "Data write at " + dstM + ", data read at " + srcM + ", length = 4 bytes.";
		}
	}
}
