//⚡ NTUA ECE, MediaLab 2022
// Kitsos Orfanopoulos el17025

// ✔ This class follows javadoc documentation protocol


package application;

/**
* This is an auxilary program. It creates an object for managing tuples.
* @author kitsorfan
*/
public final class Tuple<S,T> {

	private S data; 
	private T index;

	public Tuple(S s, T t) {
		this.data = s;
		this.index = t;
	}
	//getters
	public S getData(){		return data;}
	public T getIndex(){ 	return index;}
	
	//setters
	public void setData(S data){	this.data = data;}
	public void setIndex(T index){	this.index = index;}
	
	public void main(){}  //nothing
	}