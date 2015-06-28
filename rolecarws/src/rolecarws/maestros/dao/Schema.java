/**
 * 
 */
package rolecarws.maestros.dao;

/**
 * @author FJHL
 *
 */
public class Schema {
	private String mOnLine;
	private String mOffLine;
	
	public Schema(){
		super();
	}
	
	public Schema(String onLine, String offLine){
		mOnLine = onLine;
		mOffLine = offLine;
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
			this.mOnLine = "_b";
		}
	}
}
