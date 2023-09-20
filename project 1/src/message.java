public class message {
    private String from;
    private String to;
    private String title;
    private String content;

    private Integer message_id;


    public message(String from_i, String to_i, String title_i, String content_i, Integer message_id_i) {
        from = from_i;
        to = to_i;
        title = title_i;
        content = content_i;
        message_id = message_id_i;
    }

    public String get_from(){
        return from;
    }
    public String get_to(){
        return to;
    }
    public String get_title(){
        return title;
    }
    public String get_content(){return content;}
    public Integer get_id(){return message_id;}

    public void set_from(String from_i){
        from = from_i;
    }
    public void set_to(String to_i){
        to = to_i;
    }
    public void set_title(String title_i){
        title = title_i;
    }
    public void set_content(String content_i){ content = content_i;}
    public void set_id(Integer id_i){ message_id= id_i;}

}


