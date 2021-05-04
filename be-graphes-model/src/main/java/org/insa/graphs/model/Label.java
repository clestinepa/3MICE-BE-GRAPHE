package org.insa.graphs.model;


public class Label implements Comparable <Label> {
	
	 	private Node sommet ;

	    private boolean marque ;

	    private double cout ;
	    
	    private Arc pere ;
	    
	    private boolean dansTas ;
	    
	    public Label (Node som) {
	    	this.sommet = som ;
	    	this.marque = false ;
	    	this.cout = (float)(1.0/0.0) ;
	    	this.pere = null ;
	    	this.dansTas = false ;
	    }
	    
	    //Les get  
	    public Node getSommet() {
	    	return this.sommet ;
	    }
	    
	    public boolean getMarque() {
	    	return this.marque ;
	    }
	    
	    public double getCost() {
	    	return this.cout ;
	    }
	    
	    public Arc getPere() {
	    	return this.pere;
	    }
	    
	    public Boolean getDansTas() {
	    	return this.dansTas;
	    }
	    
	    //Les set
	    public void setSommet(Node n) {
	    	this.sommet = n ;
	    }
	    
	    public void setMarque(boolean i) {
	    	this.marque = i ;
	    }
	    
	    public void setCost(double c) {
	    	this.cout = c ;
	    }
	    
	    public void setPere(Arc p) {
	    	this.pere = p ;
	    }
	    
	    public void setDansTas(boolean i) {
	    	this.dansTas = i ;
	    }
	    

		@Override //Primordial pour faire des tas avec les Label
		public int compareTo(Label other) {
			return Double.compare(this.cout,other.cout);
		}
}
