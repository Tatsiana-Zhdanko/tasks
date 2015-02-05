package com.mycompany.ftp;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class main {

    private static final String url = "ftp.mozilla.org";
    private static final String login = "anonymous";
    private static final String password = "ftp4j";
    private static final String path = "d:\\";

    public static void connection(FTPClient client) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
        client.connect(url);
        client.login(login, password);
    }

    public static void print(FTPClient client) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException {
        FTPFile[] list = client.list();
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i].getName());
        }
    }

    public static void selection(FTPClient client) throws IllegalStateException, IOException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException, FTPIllegalReplyException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Select the desired document. 1 - exit");
            String select = sc.nextLine();
            if (select.equals("1")) {
                break;
            } else {
                try {
                    printDownload(client, select);
                } catch (FTPIllegalReplyException ex) {
                    System.out.println("Breaking the rules of the FTP protocol!" + ex.getMessage());
                }
            }
        }
    }

    public static void printDownload(FTPClient client, String select) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException {
        FTPFile[] list = client.list();
        for (int i = 0; i < list.length; i++) {
            if (select.equals(list[i].getName())) {
                if (list[i].getType() == 0 || list[i].getType() == 2) {
                    client.download(list[i].getName(), new File(path + list[i].getName()));
                    print(client);
                }
                if (list[i].getType() == 1) {
                    client.changeDirectory(select);
                    print(client);
                }
            }
        }
    }

    public static void main(String[] args) {

        FTPClient client = new FTPClient();

        try {
            connection(client);
            print(client);
            selection(client);
            client.disconnect(true);
        } catch (IllegalStateException ex) {
            System.out.println("Method was called in an invalid or inappropriate time!" + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error" + ex.getMessage());;
        } catch (FTPIllegalReplyException ex) {
            System.out.println("Breaking the rules of the FTP protocol!" + ex.getMessage());
        } catch (FTPException ex) {
            System.out.println("Error codes and messages" + ex.getMessage());
        } catch (FTPDataTransferException ex) {
            System.out.println(" I/O error occurs during a data transfer attempt" + ex.getMessage());
        } catch (FTPAbortedException ex) {
            System.out.println("The abort of a ongoing data transfer operation" + ex.getMessage());
        } catch (FTPListParseException ex) {
            System.out.println("Error in the list() method in FTPClient objects when the response sent by the server to a FTP list command is not parseable through the known parsers.  " + ex.getMessage());
        }
    }
}
