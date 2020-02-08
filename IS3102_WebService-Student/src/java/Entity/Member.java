
package Entity;

import java.util.ArrayList;

public class Member {
    private Long id;
    private String name;
    private String email;
    private Integer loyaltyPoints;
    private ArrayList<String> wishList;
    private Double cumulativeSpending;
    private String phone;
    private String address;
    private String city;
    private Integer securityQuestion;
    private String securityAnswer;
    private Integer age;
    private Integer income;

    public Member(){
    }
    
    public Member(Long aId,int aAge, String aName, String aEmail, String aPhone, String aAddress, String aCity, int aIncome, int aSecurityQuestion, String aSecurityAnswer) {
        this.id = aId;
        this.age = aAge;
        this.name = aName;
        this.email = aEmail;
        this.phone = aPhone;
        this.address = aAddress;
        this.city = aCity;
        this.income = aIncome;
        this.securityQuestion = aSecurityQuestion;
        this.securityAnswer = aSecurityAnswer;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(Integer securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public ArrayList<String> getWishList() {
        return wishList;
    }

    public void setWishList(ArrayList<String> wishList) {
        this.wishList = wishList;
    }

    public Double getCumulativeSpending() {
        return cumulativeSpending;
    }

    public void setCumulativeSpending(Double cumulativeSpending) {
        this.cumulativeSpending = cumulativeSpending;
    }
    
    
}
