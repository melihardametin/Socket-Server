import java.io.*;  
import java.net.*; 
import java.util.Scanner;

public class client {  
    public static void main(String[] args) {
        Socket socket = null;
        InputStreamReader inputstreamreader = null;
        OutputStreamWriter outputstreamwriter = null;
        BufferedReader bufferedreader = null;
        BufferedWriter bufferedwriter = null;
        try{      
            socket=new Socket("localhost",4444);
            System.out.println("connection established");
            inputstreamreader = new InputStreamReader(socket.getInputStream());
            outputstreamwriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedreader = new BufferedReader(inputstreamreader);
            bufferedwriter = new BufferedWriter(outputstreamwriter);

            while(true) {
                System.out.println("Enter your user id: ");
                Scanner scanner = new Scanner(System.in);
                String msg = "LOGIN//" + scanner.nextLine();
                System.out.println("Enter password: ");
                msg = msg + "//" + scanner.nextLine();

                bufferedwriter.write(msg);
                bufferedwriter.newLine();
                bufferedwriter.flush();

                String answ = bufferedreader.readLine();

                String[] words = answ.split("//");
                String response = words[0];




                boolean is_admin = false;
                if (response.equals("WELLCOMEadmin") || response.equals("WELLCOME")) {
                    if (response.equals("WELLCOMEadmin")) {
                        is_admin = true;
                        System.out.println("Wellcome admin.");
                    } else {
                        System.out.println("Wellcome.");
                        is_admin = false;
                    }


                    while (true) {
                        if (is_admin) System.out.println("Type the operation: (1:send message, 2:inbox, 3:outbox, 4:delete mesage, 5:update user, 6:remove user, 7:list users, 8:add user, 9:logout)");
                        else System.out.println("Type the operation: (1:send message, 2:inbox, 3:outbox, 4:delete message, 9:logout)");
                        scanner = new Scanner(System.in);
                        String num = scanner.nextLine();

                        if (is_admin) {
                            if (!(num.equals("1") ||num.equals("2") ||num.equals("3") ||num.equals("4") ||num.equals("5") ||num.equals("6") ||num.equals("7") ||num.equals("8") ||num.equals("9"))) {
                                System.out.println("Invalid operation");
                                continue;
                            }
                        }
                        else {
                            if (!(num.equals("1") ||num.equals("2") ||num.equals("3") ||num.equals("4") ||num.equals("9"))) {
                                System.out.println("Invalid operation");
                                continue;
                            }
                        }

                        if (num.equals("1")) {
                            System.out.println("Send message to: ");
                            scanner = new Scanner(System.in);
                            String to = scanner.nextLine();
                            System.out.println("Message title: ");
                            scanner = new Scanner(System.in);
                            String title = scanner.nextLine();
                            System.out.println("Message content: ");
                            scanner = new Scanner(System.in);
                            String content = scanner.nextLine();

                            bufferedwriter.write("SENDMSG//" + words[1] + "//" + to + "//" + title + "//" + content);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                            answ = bufferedreader.readLine();

                            if (answ.equals("No such a user anymore.")) {
                                System.out.println("No such a user anymore.");
                                break;
                            }

                            if (answ.equals("No such a user")) System.out.println("No such a user. ");
                            else System.out.println("Message sent successfully ");
                        }
                        else if (num.equals("2")) {
                            bufferedwriter.write("INBOX//" + words[1]);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                            answ = bufferedreader.readLine();

                            if (answ.equals("No such a user anymore.")) {
                                System.out.println("No such a user anymore.");
                                break;
                            }

                            if (answ.equals("")) {
                                System.out.println("No message.");
                                continue;
                            }
                            String[] result = answ.split("::");
                            for (String i : result) {
                                String a = i;
                                String[] x = a.split("//");
                                System.out.println("sender: " + x[0]);
                                System.out.println("title: " + x[1]);
                                System.out.println("message: " + x[2]);
                                System.out.println("**************");
                            }
                        }

                        else if (num.equals("3")) {
                            bufferedwriter.write("OUTBOX//" + words[1]);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                            answ = bufferedreader.readLine();

                            if (answ.equals("No such a user anymore.")) {
                                System.out.println("No such a user anymore.");
                                break;
                            }

                            if (answ.equals("")) {
                                System.out.println("No message sent.");
                                continue;
                            }
                            String[] result = answ.split("::");
                            for (String i : result) {
                                String a = i;
                                String[] x = a.split("//");
                                System.out.println("to: " + x[0]);
                                System.out.println("title: " + x[1]);
                                System.out.println("message: " + x[2]);
                                System.out.println("**************");
                            }
                        }

                        else if (num.equals("8")) {
                            System.out.println("***Write the new user info***");
                            System.out.println("id: ");
                            scanner = new Scanner(System.in);
                            String id = scanner.nextLine();
                            System.out.println("password for new user: ");
                            scanner = new Scanner(System.in);
                            String password = scanner.nextLine();
                            System.out.println("name: ");
                            scanner = new Scanner(System.in);
                            String name = scanner.nextLine();
                            System.out.println("surname: ");
                            scanner = new Scanner(System.in);
                            String surname = scanner.nextLine();
                            System.out.println("birth date(yyyy-mm-dd): ");
                            scanner = new Scanner(System.in);
                            String birth = scanner.nextLine();
                            System.out.println("gender (M for male, F for female): ");
                            scanner = new Scanner(System.in);
                            String gender = scanner.nextLine();
                            System.out.println("mail: ");
                            scanner = new Scanner(System.in);
                            String mail = scanner.nextLine();
                            System.out.println("city: ");
                            scanner = new Scanner(System.in);
                            String city = scanner.nextLine();
                            System.out.println("add as admin? ('y' for yes, 'n' for no): ");
                            scanner = new Scanner(System.in);
                            String admin = scanner.nextLine();

                            bufferedwriter.write("ADDUSER//" + id + "//" + password + "//" + name + "//" + surname + "//" + birth + "//" + gender + "//" + mail + "//" + city + "//" + admin + "//" + words[1]);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                            answ = bufferedreader.readLine();

                            if (answ.equals("No such a user anymore.")) {
                                System.out.println("No such a user anymore.");
                                break;
                            }

                            if (answ.equals("taken")) System.out.println("User id is already taken.");
                            else if (answ.equals("wrong format")) System.out.println("Wrong format input.");
                            else System.out.println("User added successfully.");
                        }

                        else if (num.equals("5")) {
                            System.out.println("Enter the id of the user you want to update:");
                            scanner = new Scanner(System.in);
                            String id = scanner.nextLine();
                            System.out.println("new password: ");
                            scanner = new Scanner(System.in);
                            String password = scanner.nextLine();
                            System.out.println("new name: ");
                            scanner = new Scanner(System.in);
                            String name = scanner.nextLine();
                            System.out.println("new surname: ");
                            scanner = new Scanner(System.in);
                            String surname = scanner.nextLine();
                            System.out.println("new birth date(yyyy-mm-dd)': ");
                            scanner = new Scanner(System.in);
                            String birth = scanner.nextLine();
                            System.out.println("new gender (M for male, F for female): ");
                            scanner = new Scanner(System.in);
                            String gender = scanner.nextLine();
                            System.out.println("new mail: ");
                            scanner = new Scanner(System.in);
                            String mail = scanner.nextLine();
                            System.out.println("new city: ");
                            scanner = new Scanner(System.in);
                            String city = scanner.nextLine();
                            System.out.println("update as admin? ('y' for yes, 'n' for no): ");
                            scanner = new Scanner(System.in);
                            String admin = scanner.nextLine();

                            bufferedwriter.write("UPDATE//" + id + "//" + password + "//" + name + "//" + surname + "//" + birth + "//" + gender + "//" + mail + "//" + city + "//" + admin + "//" + words[1]);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();

                            answ = bufferedreader.readLine();

                            if (answ.equals("No such a user anymore.")) {
                                System.out.println("No such a user anymore.");
                                break;
                            }

                            if (answ.equals("no_user")) System.out.println("No such a user. ");
                            else System.out.println("Update successful. ");
                        }

                        else if (num.equals("6")) {
                            System.out.println("Enter the id of the user you want to delete:");
                            scanner = new Scanner(System.in);
                            String id = scanner.nextLine();

                            bufferedwriter.write("DELETE//" + id + "//" + words[1]);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                            answ = bufferedreader.readLine();

                            if (answ.equals("No such a user anymore.")) {
                                System.out.println("No such a user anymore.");
                                break;
                            }

                            if (answ.equals("no_user")) System.out.println("No such a user. ");
                            else System.out.println("Deleted. ");
                        }

                        else if (num.equals("7")) {
                            bufferedwriter.write("LIST//" + words[1]);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                            answ = bufferedreader.readLine();

                            if (answ.equals("No such a user anymore.")) {
                                System.out.println("No such a user anymore.");
                                break;
                            }

                            String[] result = answ.split("::");
                            for (String i : result) {
                                String a = i;
                                String[] x = a.split("//");

                                System.out.println("id: " + x[0]);
                                System.out.println("password: " + x[1]);
                                System.out.println("name: " + x[2]);
                                System.out.println("surname: " + x[3]);
                                System.out.println("birth date: " + x[4]);
                                System.out.println("gender: " + x[5]);
                                System.out.println("mail: " + x[6]);
                                System.out.println("city: " + x[7]);
                                System.out.println("**************");
                            }
                        }
                        else if (num.equals("4")) {
                            System.out.println("Messages:");
                            System.out.println("*************");
                            bufferedwriter.write("INBOX//" + words[1]);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                            answ = bufferedreader.readLine();

                            if (answ.equals("No such a user anymore.")) {
                                System.out.println("No such a user anymore.");
                                break;
                            }

                            if (answ.equals("")) {
                                System.out.println("No message.");
                                continue;
                            }
                            String[] result = answ.split("::");
                            for (String i : result) {
                                String a = i;
                                String[] x = a.split("//");
                                System.out.println("Message no: " + x[3]);
                                System.out.println("from: " + x[0]);
                                System.out.println("title: " + x[1]);
                                System.out.println("message: " + x[2]);
                                System.out.println("**************");
                            }
                            System.out.println("Select the message you want to delete(num):");
                            Scanner scan = new Scanner(System.in); // add no message found.
                            String content = scan.nextLine();
                            bufferedwriter.write("DELETEMSG//" + words[1] + "//" + content);
                            bufferedwriter.newLine();
                            bufferedwriter.flush();
                            answ = bufferedreader.readLine();
                            if (answ.equals("invalid_msg")) System.out.println("No such a message");
                            else if (answ.equals("invalid")) System.out.println("Invalid input");
                            else if (answ.equals("ok")) System.out.println("Deleted");

                        } else if (num.equals("9")) {
                            is_admin = false;
                            break;
                        } else {
                            System.out.println("Invalid operation");
                        }
                    }
                }

                else if (answ.equals("Wrong password.")) {
                    System.out.println("Wrong password.");
                }
                else if (answ.equals("User not found.")){
                    System.out.println("User not found.");
                }

            }
        }
        catch(Exception e){System.out.println(e);}  
    }  
}  