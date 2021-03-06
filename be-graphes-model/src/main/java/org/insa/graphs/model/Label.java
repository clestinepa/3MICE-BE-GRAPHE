package org.insa.graphs.model;


public class Label implements Comparable <Label> {
	
	 	private Node sommet ;

	    private boolean marque ;

	    private double cout ;
	    
	    private Arc pere ;
	    
	    private boolean dansTas ; //permet de savoir si le sommet est dans le tas
	    
	    public Label (Node som) {
	    	this.sommet = som ;
	    	this.marque = false ;
	    	this.cout = (float)(1.0/0.0) ; //+inf
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
	    
	    /*Juste pour être redéfinie par les héritiers (i.e LabelStar)*/
	    public double getTotalCost() {
	    	return this.cout ;
	    }
	    

		@Override //Primordial pour faire des tas avec les Label
		public int compareTo(Label other) {
			return Double.compare(this.getTotalCost(),other.getTotalCost());
		}
}
