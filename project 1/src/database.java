import java.math.BigInteger;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class database {

    Connection conn;


    public database() {

        create_database();

    }

    public void create_database() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/wp", "postgres", "1234");

            String sqlCreate = "create table if not exists users ( "
                    + "id character varying not null, "
                    + "pwd character varying, "
                    + "name character varying, "
                    +  "surname character varying, "
                    +  "date date, "
                    +  "gender character varying, "
                    +  "mail character varying, "
                    +  "city character varying, "
                    +  "admin boolean, "
                    +  "constraint users_pkey primary key (id))";


            String sqlmessageCreate = "create table if not exists messages ("
                    + "sender character varying not null, "
                    + "receiver character varying not null, "
                    + "title character varying, "
                    + "message character varying,"
                    + "p_key bigint not null default nextval('messages_p_key_seq'::regclass), "
                    + "to_b boolean not null default true, "
                    + "constraint messages_pkey primary key (p_key))";

            Statement stmt = conn.createStatement();
            stmt.execute(sqlCreate);
            stmt.execute(sqlmessageCreate);

            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from users");
            Integer size = 0;

            while (rs.next()) {
                size = size+1;
            }
            if (size == 0) {
                java.sql.Date sqlDate = java.sql.Date.valueOf("2000-01-01");
                stm.execute("insert into users values('root','root','admin','admin','"+sqlDate+"','M','admin@mail.com','Ankara',true)");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String search(String id, String pwd) {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from users");

            while (rs.next()) {
                if ((rs.getString(1)).equals(id)) {
                    if ((rs.getString(2)).equals(pwd)) {
                        if ((rs.getBoolean(9))) {
                            return ("admin user found");
                        }
                        else return ("user found");
                    }
                    return ("Wrong password.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ("User not found");
    }

    public boolean savemsg(message newmsg) {
        database d = new database();
        if (!d.is_there_user(newmsg.get_to())) {
            return false;
        }

        try {
            Statement stm = conn.createStatement();
            String from = newmsg.get_from();
            String to = newmsg.get_to();
            String title = newmsg.get_title();
            String content = newmsg.get_content();
            stm.execute("insert into messages values('"+from+"','"+to+"','"+title+"','"+content+"')");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean is_there_user(String id ) {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from users");
            while (rs.next()) {
                if ((rs.getString(1)).equals(id)) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<message> inbox(String id) {
        List<message> message_list = new ArrayList<message>();
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from messages where to_b = "+true+" ");

            while (rs.next()) {
                if (rs.getString(2).equals(id)) {
                    message tmp_message = new message(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                    message_list.add(tmp_message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message_list;
    }

    public List<message> outbox(String id) {
        List<message> message_list = new ArrayList<message>();
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from messages");

            while (rs.next()) {
                if (rs.getString(1).equals(id)) {
                    message tmp_message = new message(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                    message_list.add(tmp_message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message_list;
    }

    public String add_user(user x) {

        try {
            Statement stm = conn.createStatement();
            String id = x.get_id();
            String password = x.get_password();
            String name = x.get_name();
            String surname = x.get_surname();
            String birth = x.get_date();
            String[] birt = birth.split("-");
            Date aa = new Date(Integer.valueOf(birt[0]), Integer.valueOf(birt[1]), Integer.valueOf(birt[2]));
            long diff = 59960752146000L;
            long milis = aa.getTime() - diff;
            String gender = x.get_gender();
            String mail = x.get_mail();
            String city = x.get_city();
            boolean admin = x.get_admin();
            java.sql.Date sqlDate = new java.sql.Date(milis);
            stm.execute("insert into users values('"+id+"','"+password+"','"+name+"','"+surname+"','"+sqlDate+"','"+gender+"','"+mail+"','"+city+"',"+admin+")");
            return "ok";

        }

        catch (Exception e) {
            e.printStackTrace();
            return "wrong format";
        }
    }

    public void update_user(user x) {

        try {
            Statement stm = conn.createStatement();
            String id = x.get_id();
            String password = x.get_password();
            String name = x.get_name();
            String surname = x.get_surname();
            String birth = x.get_date();
            String[] birt = birth.split("-");
            Date aa = new Date(Integer.valueOf(birt[0]), Integer.valueOf(birt[1]), Integer.valueOf(birt[2]));
            long diff = 59960752146000L;
            long milis = aa.getTime() - diff;
            String gender = x.get_gender();
            String mail = x.get_mail();
            String city = x.get_city();
            boolean admin = x.get_admin();
            java.sql.Date sqlDate = new java.sql.Date(milis);
            String sql = "update users set pwd='"+password+"',name='"+name+"',surname='"+surname+"',date='"+sqlDate+"',gender='"+gender+"',mail='"+mail+"',city='"+city+"',admin="+admin+" where id='"+id+"'  " ;
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.executeUpdate();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

     public void delete_user(String id) {


        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM Users WHERE id = '"+id+"' ");
            st.executeUpdate();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<user> list() {

        String result = "";
        List<user> users = new ArrayList<user>();
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from users");

            while (rs.next()) {
                user tmp_user = new user(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),rs.getString(7), rs.getString(8), rs.getBoolean(9));
                users.add(tmp_user);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void delete_mesage(String id, Integer num) {

        BigInteger a = BigInteger.valueOf(num);
        try {
            String sql = "update messages set to_b = "+false+" where p_key='"+a+"'  " ;
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean valid_choice(String id, Integer num) {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from messages");

            while (rs.next()) {
                if (rs.getString(2).equals(id) && rs.getInt(5)==num) {
                    return true;
                }
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
