import java.util.ArrayList;
import java.util.List;

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
	
	private ArrayList<int[]> chache;
	private int data[];
	
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
		
		chache = new ArrayList<int[]>();
		
		data = new int[cacheSize];
	}
	
	public String getAssocName() {
		if(associativityNum == 1)
			return "direct";
		else 
			return associativityNum + "-way";
	}
	
	//Calculations
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

	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
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

	
	
	
}
