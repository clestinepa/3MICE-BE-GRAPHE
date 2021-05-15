package org.insa.graphs.model;


public class LabelStar extends Label implements Comparable <Label> {
	
	protected double cout_estime ;
	
	public LabelStar(Node Sommet) {
		super(Sommet);
		this.cout_estime = (float)(1.0/0.0) ; //+inf
	 }
	
	public double getCostEstime() {
		return this.cout_estime ;
	}
	
	public void setCostEstime(double c) {
		this.cout_estime = c ;
	}
	
	
	/*Permet de modifier le CompareTo indirectement*/
	public double getTotalCost() {
    	return this.getCost() + this.getCostEstime() ;
    }

}
