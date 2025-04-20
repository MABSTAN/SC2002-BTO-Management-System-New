package entity;

public abstract class User {
    private String name;
    private String nric;
    private String password;
    private int age;
    public enum MaritalStatus{
        SINGLE,
        MARRIED
    }
    private MaritalStatus maritalStatus;

    //Constructor
    public User(){

    }

    //Methods
    public void changePassword(String password){
        this.setPassword(password);
    }

    public void login(){

    }

    //Getter Setter
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setNric(String nric){
        this.nric = nric;
    }

    public String getNric(){
        return this.nric;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getAge(){
        return this.age;
    }
    public void setMaritalStatus(String input) {
        try {
            this.maritalStatus = MaritalStatus.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid marital status: " + input);
            this.maritalStatus = null; // or set a default value like MaritalStatus.SINGLE
        }
    }

    public MaritalStatus getMaritalStatus (){
        return this.maritalStatus;
    }

}
