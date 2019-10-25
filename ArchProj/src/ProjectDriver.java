
public class ProjectDriver {
	
	public static void main(String[] args) {
		
		// Initialize Command Line Arguments
		String traceFileName = new String(args[2]);
		int cacheSizeInKB = Integer.parseInt(args[4]);
		int blockSizeInBytes = Integer.parseInt(args[6]);
		int associativityNum = Integer.parseInt(args[8]);
		String rPolicy = new String(args[10]);
		String associativityName;
		
		System.out.println("Cache Simulator CS 3853 Fall 2019 - Group #4\n");
		
		// Parses through each of the command line arguments
		System.out.print("Cmd Line: ");
		for(int i = 0; i < args.length; i++) {
			System.out.print(args[i] + " ");
		}
		
		int cacheSizeInBytes = cacheSizeInKB * 1024; // Convert cache size KB to bytes
		int numBlocksInKB = (cacheSizeInBytes / blockSizeInBytes) / 1024; // Get number of blocks in KB
		int bitsInBlockOffset = (int) ((Math.log(blockSizeInBytes))/(Math.log(2))); // Get the exponent of the power of 2 for number of bits in block offset
		int numBlocksPwr2 = (int) (Math.log(numBlocksInKB) / Math.log(2)) + 10; // Adding 10 since a KB is 2^10
		
		System.out.println("\nTrace File: " + traceFileName);
		System.out.println("Cache Size: " + cacheSizeInKB + " KB");
		System.out.println("Block Size: " + blockSizeInBytes + " bytes");
		if(associativityNum == 1)
			associativityName = "direct";
		else 
			associativityName = associativityNum + "-way";
		System.out.println("Associativity: " + associativityName);
		System.out.println("R-Policy: " + rPolicy);
		
		System.out.println("\nGeneric: ");
		System.out.println("Cache Size: " + cacheSizeInKB + " KB");
		System.out.println("Block Size: " + blockSizeInBytes + " bytes");
		System.out.println("Associativity: " + associativityNum);
		System.out.println("Policy: " + rPolicy);
		

		// Note to selves, remove ceiling function when we figure out how to get a precise answer
		int cacheIndexBits = (int) Math.ceil(Math.log((double) cacheSizeInBytes / (double) (blockSizeInBytes * associativityNum)) / Math.log(2));
		int tagBits = 32 - cacheIndexBits - bitsInBlockOffset;
		int totalNumIndicesInBytes = (int) (Math.pow(2, cacheIndexBits));
		int totalNumIndicesInKB = totalNumIndicesInBytes / 1024;
		int overheadInBytes = associativityNum * (1 + tagBits) * (totalNumIndicesInBytes / 8);
		int overheadInKB = overheadInBytes / 1024;
		int impMemorySizeInBytes = overheadInBytes + cacheSizeInBytes;
		int impMemorySizeInKB = impMemorySizeInBytes / 1024;
		System.out.println("----- Calculated Values -----");
		System.out.println("Total #Blocks: " + numBlocksInKB + " KB " + "(2^ " + numBlocksPwr2 + ")");
		System.out.println("Tag Size: " + tagBits + " bits");
		System.out.println("Index Size: " + cacheIndexBits + " bits, Total Indices: " + totalNumIndicesInKB + " KB");
		System.out.println("Overhead Memory Size: " + overheadInBytes + " bytes (or " + overheadInKB + " KB)");
		System.out.println("Implementation Memory Size: " + impMemorySizeInBytes + " bytes (or " + impMemorySizeInKB + " KB)");
		System.out.println("----- Results -----");
		System.out.println("Cache Hit Rate: ***%");
		System.out.println("CPI: ");
		
		
	}

}
