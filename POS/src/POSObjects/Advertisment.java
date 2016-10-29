package POSObjects;

public class Advertisment {
	private String name;
	private String email;
	private String barcode;
	private String file_loc;

	public Advertisment(String name,String email, String barcode,String imagepath){
		this.name=name;
		this.email=email;
		this.barcode=barcode;
		this.file_loc=imagepath;
	}
	public String getImagePath(){return this.file_loc;}
	public String getName(){
		return this.name;
	}
	public String getEmail(){
		return this.email;
	}
	public String getBarcode(){
		return this.barcode;
	}
}
