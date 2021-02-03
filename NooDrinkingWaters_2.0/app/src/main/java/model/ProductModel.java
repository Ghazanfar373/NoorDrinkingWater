package model;

public class ProductModel{
	private String image;
	private Object unit;
	private String price;
	private String name;
	private String id;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setUnit(Object unit){
		this.unit = unit;
	}

	public Object getUnit(){
		return unit;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"ProductModel{" + 
			"image = '" + image + '\'' + 
			",unit = '" + unit + '\'' + 
			",price = '" + price + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}
