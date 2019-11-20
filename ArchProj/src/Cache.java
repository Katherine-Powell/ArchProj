import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cache {
	
	//Assume each value is in bytes
	private int cacheSize;
	private int blockSize;
	private int associativityNum;
	private String rPolicy;
	
	private int numBlocks;
	
	private int blockOffsetBits;
	private int indexBits;
	private int tagBits;
	
	private int numIndicies;
	private int overhead;
	private int impMemorySize;
	
	private ArrayList<int[][]> cache;
	private int blockReplace[];
	
	private int numHits;
	private int numCompulsoryMisses;
	private int numConflictMisses;

	private int numMisses;
	private int clockCycles;
	
	private Random generator;
	
	private int instructionCount;
	
	public Cache(String[] args) {
		//Given
		cacheSize = Integer.parseInt(args[4]) * 1024; //convert to bytes
		blockSize = Integer.parseInt(args[6]);
		associativityNum = Integer.parseInt(args[8]);
		rPolicy = new String(args[10]);
		
		//Calculate		
		numBlocks = calcNumBlocks();
		blockOffsetBits = calcBlockOffset();
		indexBits = calcIndexBits();
		tagBits = calcTagBits();
		numIndicies = calcNumIndicies();
		overhead = calcOverhead();
		impMemorySize = calcImpMemorySize();
		
		cache = new ArrayList<int[][]>();
		for(int i=0; i<associativityNum; i++)
		{
			cache.add(new int[numIndicies][4]);
		}
		
		blockReplace = new int[numIndicies];
		generator = new Random(System.currentTimeMillis());
		instructionCount = 0;
		clockCycles = 0;
		
	}
	
	public boolean isInCache(long address){
		int addressIndex = getIndexFromAddress(address);
		int tag = getTagFromAddress(address);
		int block[][];
	
		for(int i=0; i<associativityNum; i++) {
			block = cache.get(i);
			if(block[addressIndex][0] == addressIndex && block[addressIndex][2] == tag){
				clockCycles++;
				return true;
			}
		}
		return false;
	}
	
	public void addRoundRobinEntry(long address, int length){
		double numReads = blockSize/4.0;
	
		int addressIndex = getIndexFromAddress(address);
		int tag = getTagFromAddress(address);
		int offset = getOffsetFromAddress(address);
		
		int currentBlockReplace = blockReplace[addressIndex];
		
		if(address == 0)
			return;
		
		if(!isInCache(address)){
			int block[][] = cache.get(blockReplace[addressIndex]);
			
			if(block[addressIndex][1] != 1){
				numMisses++;
				numCompulsoryMisses++;
				clockCycles += 3 * numReads;
			}
			else {
				numMisses++;
				numConflictMisses++;
			}
				
			block[addressIndex][0] = addressIndex;
			block[addressIndex][1] = 1;
			block[addressIndex][2] = tag;
			// if data column was needed... put here
			blockReplace[addressIndex] ++;
			if(blockReplace[addressIndex] == associativityNum)
				blockReplace[addressIndex] = 0;			
		}
		else { // cache hit
			numHits++;
			clockCycles++;
		}
		
		int bitsLeftover = extraBits(offset, length); 
		
		if(bitsLeftover > 0) {
			manualReplace(address, bitsLeftover, currentBlockReplace); // Call if data takes up more than one row
		}
		
	}

	public void manualReplace(long address, int length, int blockReplace){
		double numReads = blockSize/4.0;
	
		int addressIndex = getIndexFromAddress(address);
		addressIndex += 1;
		if(addressIndex >= numIndicies)
			addressIndex = 0;
		int tag = getTagFromAddress(address);
		int offset = getOffsetFromAddress(address);
		
		if(address == 0)
			return;
		
		int block[][] = cache.get(blockReplace);
		if(block[addressIndex][1] != 1){
			numMisses++;
			numCompulsoryMisses++;
			clockCycles += 3 * numReads;
		}

		
		block[addressIndex][0] = addressIndex;
		block[addressIndex][1] = 1;
		block[addressIndex][2] = tag;
		// if data column was needed... put here
		
	}
	
	public void addRandomEntry(long address, int length){
		double numReads = blockSize/4.0;
		
		int addressIndex = getIndexFromAddress(address);
		int tag = getTagFromAddress(address);
		int offset = getOffsetFromAddress(address);
		
		int currentBlockReplace = blockReplace[addressIndex];
		
		if(address == 0)
			return;
		
		if(!isInCache(address)){
			int block[][] = cache.get(generator.nextInt(associativityNum));
			if(block[addressIndex][1] != 1) {
				numMisses++;
				clockCycles += 3 * numReads;
			}
					
			block[addressIndex][0] = addressIndex;
			block[addressIndex][1] = 1;
			block[addressIndex][2] = tag;
			// if data column was needed... put here	
		}
		else {
			numHits++;
			clockCycles++;
		}
		
		int bitsLeftover = extraBits(offset, length); 
		
		if(bitsLeftover > 0) {
			manualReplace(address, bitsLeftover, currentBlockReplace); // Call if data takes up more than one row
		}
	}
	
	public int extraBits(int offset, int length) {
		String binaryLimit = new String(); // string representation of binary number
		int limit;
		for(int i =0; i<blockOffsetBits; i++){
			binaryLimit += 1; // put ones in actual string
		}
		limit = Integer.parseUnsignedInt(binaryLimit, 2); // convert binary number to decimal
		
		return (offset + length) - limit;
	}
	
	public double getCacheHitRatio(){
		return (double)numHits/(double)(numHits+numMisses) * 100;
	}
	
	public int getOffsetFromAddress(long address) {
		String andNumS = new String();
		for(int i =0; i<blockOffsetBits; i++){
			andNumS += 1;
		}
		int andNumInt = Integer.parseInt(andNumS, 2);
		return (int)(address & andNumInt);
	}
	
	public int getIndexFromAddress(long address) {
		address = address >> blockOffsetBits;
		
		String andNumS = new String();
		for(int i =0; i<indexBits; i++){
			andNumS += 1;
		}
		int andNumInt = Integer.parseInt(andNumS, 2);
		return (int)(address & andNumInt);
	}
	
	public int getTagFromAddress(long address){
		return (int)(address >> (indexBits + blockOffsetBits));
	}
	
	public String getAssocName() {
		if(associativityNum == 1)
			return "direct";
		else 
			return associativityNum + "-way";
	}
	
	//Cache Info Calculations
	private int calcNumBlocks() {
		return cacheSize / blockSize;
	}
	
	private int calcBlockOffset() {
		return Util.logBase2(blockSize);
	}
	
	private int calcIndexBits() {
		return (int) Util.logBase2((double)cacheSize / (blockSize * associativityNum));
	}
	
	private int calcTagBits() {
		return 32 - indexBits - blockOffsetBits;
	}
	
	private int calcNumIndicies() {
		return (int) (Math.pow(2, indexBits));
	}
	
	private int calcOverhead() {
		 return associativityNum * (1 + tagBits) * (numIndicies / 8);
	}
	
	private int calcImpMemorySize() {
		return overhead + cacheSize;
	}
	
	//Getters and Setters
	
	public int getInstructionCount() {
		return instructionCount;
	}

	public void setInstructionCount(int instructionCount) {
		this.instructionCount = instructionCount;
	}
	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getAssociativityNum() {
		return associativityNum;
	}

	public void setAssociativityNum(int associativityNum) {
		this.associativityNum = associativityNum;
	}

	public String getRPolicy() {
		return rPolicy;
	}

	public void setrPolicy(String rPolicy) {
		this.rPolicy = rPolicy;
	}

	public int getNumBlocks() {
		return numBlocks;
	}

	public void setNumBlocks(int numBlocks) {
		this.numBlocks = numBlocks;
	}

	public int getBlockOffset() {
		return blockOffsetBits;
	}

	public void setBlockOffset(int blockOffset) {
		this.blockOffsetBits = blockOffset;
	}

	public int getBlockOffsetBits() {
		return blockOffsetBits;
	}

	public void setBlockOffsetBits(int blockOffsetBits) {
		this.blockOffsetBits = blockOffsetBits;
	}

	public int getIndexBits() {
		return indexBits;
	}

	public void setIndexBits(int indexBits) {
		this.indexBits = indexBits;
	}

	public int getTagBits() {
		return tagBits;
	}

	public void setTagBits(int tagBits) {
		this.tagBits = tagBits;
	}

	public int getNumIndicies() {
		return numIndicies;
	}

	public void setNumIndicies(int numIndicies) {
		this.numIndicies = numIndicies;
	}

	public int getOverhead() {
		return overhead;
	}

	public void setOverhead(int overhead) {
		this.overhead = overhead;
	}

	public int getImpMemorySize() {
		return impMemorySize;
	}

	public void setImpMemorySize(int impMemorySize) {
		this.impMemorySize = impMemorySize;
	}

	public int getClockCycles() {
		return clockCycles;
	}

	public void setClockCycles(int clockCycles) {
		this.clockCycles = clockCycles;
	}
	public int getNumCompulsoryMisses() {
		return numCompulsoryMisses;
	}

	public void setNumCompulsoryMisses(int numCompulsoryMisses) {
		this.numCompulsoryMisses = numCompulsoryMisses;
	}

	public int getNumConflictMisses() {
		return numConflictMisses;
	}

	public void setNumConflictMisses(int numConflictMisses) {
		this.numConflictMisses = numConflictMisses;
	}
	
	public int getNumHits() {
		return numHits;
	}

	public void setNumHits(int numHits) {
		this.numHits = numHits;
	}

	public int getNumMisses() {
		return numMisses;
	}

	public void setNumMisses(int numMisses) {
		this.numMisses = numMisses;
	}

}
