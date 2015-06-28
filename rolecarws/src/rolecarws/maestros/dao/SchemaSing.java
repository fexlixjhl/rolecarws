/**
 * 
 */
package rolecarws.maestros.dao;

/**
 * @author FJHL
 *
 */
public enum SchemaSing {
	INSTANCE;
	//data
	private String mOnLine;
	private String mOffLine;
	
	private SchemaSing(){
		mOffLine = "";
		mOnLine = "";
	}
	
	private SchemaSing(String onLine, String offLine){
		mOnLine = onLine;
		mOffLine = offLine;
	}
	
	 // Static getter
    public static SchemaSing getInstance()
    {
        return INSTANCE;
    }
    
	/**
	 * @return the mOnLine
	 */
	public String getOnLine() {
		return mOnLine;
	}
	/**
	 * @param mOnLine the mOnLine to set
	 */
	public void setOnLine(String mOnLine) {
		this.mOnLine = "";
		if ("B".equals(mOnLine)){
			this.mOnLine = "_b";
		}
	}
	/**
	 * @return the mOffLine
	 */
	public String getOffLine() {
		return mOffLine;
	}
	/**
	 * @param mOffLine the mOffLine to set
	 */
	public void setOffLine(String mOffLine) {
		this.mOffLine = "";
		if ("B".equals(mOffLine)){
			this.mOffLine = "_b";
		}
	}
}
