import java.io.*;
import java.net.*;
import java.util.*;


public class server {

    private static database dtb = new database();

    public static void main(String[] args)
    {
        ServerSocket server = null;

        try {

            server = new ServerSocket(4444);
            server.setReuseAddress(true);

            while (true) {

                Socket client = server.accept();
                System.out.println("New client connected"
                        + client.getInetAddress()
                        .getHostAddress());
                ClientHandler clientSock
                        = new ClientHandler(client);

                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run()
        {
            Socket socket = null;
            ServerSocket ss = null;
            InputStreamReader inputstreamreader = null;
            OutputStreamWriter outputstreamwriter = null;
            BufferedReader bufferedreader = null;
            BufferedWriter bufferedwriter = null;



            while(true) {
                try {
                    socket = clientSocket;


                    inputstreamreader = new InputStreamReader(socket.getInputStream());
                    outputstreamwriter = new OutputStreamWriter(socket.getOutputStream());
                    bufferedreader = new BufferedReader(inputstreamreader);
                    bufferedwriter = new BufferedWriter(outputstreamwriter);

                    while(true) {
                        String msg = bufferedreader.readLine();
                        String[] words = msg.split("//");
                        String operation = words[0];

                        if (operation.equals("LOGIN")) {
                            String id = words[1];
                            String password = words[2];
                            String x = dtb.search(id,password);

                            if (x.equals("user found")) {
                                bufferedwriter.write("WELLCOME//"+id);
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                            }
                            else if (x.equals("admin user found")) {
                                bufferedwriter.write("WELLCOMEadmin//"+id);
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                            }
                            else if (x.equals("Wrong password.")) {
                                bufferedwriter.write("Wrong password.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                            }
                            else  {
                                bufferedwriter.write("User not found.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                            }
                        }


                        else if (operation.equals("LOGOUT")) {
                            continue;
                        }

                        else if (operation.equals("SENDMSG")) {
                            if (!dtb.is_there_user(words[1])) {
                                bufferedwriter.write("No such a user anymore.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            String from = words[1];
                            String to = words[2];
                            String title = words[3];
                            String content = words[4];

                            message newmsg = new message(from, to,title,content,0);
                            boolean x = dtb.savemsg(newmsg);;
                            if (!x) bufferedwriter.write("No such a user");
                            else bufferedwriter.write("success");
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                        }

                        else if (operation.equals("ADDUSER")) {
                            if (!dtb.is_there_user(words[10])) {
                                bufferedwriter.write("No such a user anymore.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }

                            String id = words[1];
                            String password = words[2];
                            String name = words[3];
                            String surname = words[4];
                            String date = words[5];
                            String gender = words[6];
                            String mail = words[7];
                            String city = words[8];
                            boolean admin = (Objects.equals(words[9], "y"));

                            if (dtb.is_there_user(id)) {
                                bufferedwriter.write("taken");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            else {
                                user newuser = new user(id,password,name,surname, date ,gender, mail, city, admin);
                                String result = dtb.add_user(newuser);
                                if (result == "wrong format") {
                                    bufferedwriter.write("wrong format");
                                    bufferedwriter.newLine();
                                    bufferedwriter.flush();
                                }
                                else {
                                    bufferedwriter.write("ok");
                                    bufferedwriter.newLine();
                                    bufferedwriter.flush();
                                }
                            }
                        }

                        else if (operation.equals("INBOX")) {
                            if (!dtb.is_there_user(words[1])) {
                                bufferedwriter.write("No such a user anymore.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            String id = words[1];
                            List<message> message_list = dtb.inbox(id);
                            String result = "";

                            for (int i = 0; i < message_list.size(); i++) {
                                message tmp_message = message_list.get(i);
                                if (result.equals("")) result = tmp_message.get_from() + "//" + tmp_message.get_title() + "//" + tmp_message.get_content()+ "//" + tmp_message.get_id();
                                else result = result + "::" + tmp_message.get_from() + "//" + tmp_message.get_title() + "//" + tmp_message.get_content()+ "//" + tmp_message.get_id();
                            }

                            bufferedwriter.write(result);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                        }

                        else if (operation.equals("OUTBOX")) {
                            if (!dtb.is_there_user(words[1])) {
                                bufferedwriter.write("No such a user anymore.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            String id = words[1];
                            List<message> message_list = dtb.outbox(id);
                            String result = "";

                            for (int i = 0; i < message_list.size(); i++) {
                                message tmp_message = message_list.get(i);
                                if (result.equals("")) result = tmp_message.get_to() + "//" + tmp_message.get_title() + "//" + tmp_message.get_content()+ "//" + tmp_message.get_id();
                                else result = result + "::" + tmp_message.get_to() + "//" + tmp_message.get_title() + "//" + tmp_message.get_content()+ "//" + tmp_message.get_id();
                            }

                            bufferedwriter.write(result);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                        }

                        else if (operation.equals("UPDATE")) {
                            if (!dtb.is_there_user(words[10])) {
                                bufferedwriter.write("No such a user anymore.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            String id = words[1];
                            String password = words[2];
                            String name = words[3];
                            String surname = words[4];
                            String birth = words[5];
                            String gender = words[6];
                            String mail = words[7];
                            String city = words[8];
                            boolean admin = (Objects.equals(words[9], "y"));


                            if (!dtb.is_there_user(id)) {
                                bufferedwriter.write("no_user");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            else {
                                bufferedwriter.write("ok");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                            }

                            user newuser = new user(id,password,name,surname,birth,gender, mail,city, admin);
                            dtb.update_user(newuser);
                        }

                        else if (operation.equals("DELETE")) {
                            if (!dtb.is_there_user(words[2])) {
                                bufferedwriter.write("No such a user anymore.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            String id = words[1];
                            if (!dtb.is_there_user(id)) bufferedwriter.write("no_user");
                            else bufferedwriter.write("ok");
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                            dtb.delete_user(id);
                        }

                        else if (operation.equals("LIST")) {
                            if (!dtb.is_there_user(words[1])) {
                                bufferedwriter.write("No such a user anymore.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            String result = "";
                            List<user> users = dtb.list();
                            for (int i = 0; i < users.size(); i++) {
                                user tmp_user = users.get(i);
                                if (result.equals("")) result = tmp_user.get_id() + "//" + tmp_user.get_password() + "//" + tmp_user.get_name() + "//" + tmp_user.get_surname() + "//" + tmp_user.get_date() + "//" + tmp_user.get_gender() + "//" + tmp_user.get_mail() + "//" + tmp_user.get_city();
                                else result = result + "::" + tmp_user.get_id() + "//" + tmp_user.get_password() + "//" + tmp_user.get_name() + "//" + tmp_user.get_surname() + "//" + tmp_user.get_date() + "//" + tmp_user.get_gender() + "//" + tmp_user.get_mail() + "//" + tmp_user.get_city();
                            }
                            bufferedwriter.write(result);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                        }

                        else if (operation.equals("DELETEMSG")) {
                            if (!dtb.is_there_user(words[1])) {
                                bufferedwriter.write("No such a user anymore.");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }

                            String id = words[1];
                            String num = words[2];
                            int number = 0;
                            try{
                                number = Integer.parseInt(num);
                            }
                            catch (NumberFormatException ex){
                                bufferedwriter.write("invalid");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            if (!dtb.valid_choice(id, number)) {
                                bufferedwriter.write("invalid_msg");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                                continue;
                            }
                            else {
                                bufferedwriter.write("ok");
                                bufferedwriter.newLine();
                                bufferedwriter.flush();
                            }
                            dtb.delete_mesage(id,number);
                        }

                        System.out.println("user: " + msg);
                    }
                }
                catch(Exception e){
                    System.out.println(e);}
            }
        }
    }
}
