package model;

/**
 * Created by E155399M on 20/02/17.
 */
public class Trio<T1, T2, T3> {
	public T1 one;
	public T2 two;
	public T3 three;

	public Trio(T1 t1, T2 t2, T3 t3){
		this.one = t1;
		this.two = t2;
		this.three = t3;
	}
}
