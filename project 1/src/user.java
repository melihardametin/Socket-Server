public class user {
    private String id;
    private String password;
    private String name;
    private String surname;
    private String date;
    private String gender;
    private String mail;
    private String city;
    private boolean admin;

    public user(String id_i, String password_i, String name_i, String surname_i, String date_i, String gender_i, String mail_i, String city_i, boolean admin_i){
        id = id_i;
        password = password_i;
        name = name;
        surname = surname_i;
        date = date_i;
        gender = gender_i;
        mail = mail_i;
        city = city_i;
        admin = admin_i;
    }

    public String get_id() {return id;}
    public String get_password() {return password;}
    public String get_name() {return name;}
    public String get_surname() {return surname;}
    public String get_date() {return date;}
    public String get_gender() {return gender;}
    public String get_mail() {return mail;}
    public String get_city() {return city;}
    public boolean get_admin() {return admin;}


    public void set_id(String id_i) {id = id_i;}
    public void set_password(String password_i) {password = password_i;}
    public void set_name(String name_i) {name = name_i;}
    public void set_surname(String surname_i) {surname = surname_i;}
    public void set_date(String date_i) {date = date_i;}
    public void set_gender(String gender_i) {gender = gender_i;}
    public void set_mail(String mail_i) {mail = mail_i;}
    public void set_city(String city_i) {city = city_i;}
    public void set_admin(boolean admin_i) {admin = admin_i;}

}
