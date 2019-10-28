
public class ProjectDriver {
	
	public static void main(String[] args) {
		String traceFileName = new String(args[2]);
				
		// Initialize Command Line Arguments
		Cache cache = new Cache(args);
		
		System.out.println("Cache Simulator CS 3853 Fall 2019 - Group #4\n");
		
		// Print command line arguments
		System.out.print("Cmd Line: ");
		for(int i = 0; i < args.length; i++) {
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
		
		
	}
	

}
