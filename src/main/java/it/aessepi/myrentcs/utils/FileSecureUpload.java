//package it.aessepi.myrentcs.utils;
//
//import java.io.*;
//import java.net.URL;
//import java.nio.channels.Channel;
//
//import com.jcraft.jsch.*;
//import org.apache.poi.util.IOUtils;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.websocket.Session;
//
//public class FileSecureUpload {
//    public String uploadWithKey(String ftpServer, int port, String user, String key,
//                                String fileName, File source) {
//        System.out.println("ftp serverupload port user key filename source " + ftpServer + " || " + port + " ||" + user + " || key privata || " + fileName + " ||" + source);
//        String r = "f";
//        String ftpSer = ftpServer.split("/")[0];
//        String folderName = ftpServer.split("/")[1];
//        String SFTPHOST = ftpSer;
//        int SFTPPORT = port;
//
//        String SFTPWORKINGDIR = folderName;
//
//        Session session = null;
//        Channel channel = null;
//        ChannelSftp channelSftp = null;
//
//        try {
//            JSch jsch = new JSch();
//            jsch.addIdentity(key);
//            session = jsch.getSession(user, ftpSer, SFTPPORT);
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);
//            session.connect();
//            channel = session.openChannel("sftp");
//            channel.connect();
//            channelSftp = (ChannelSftp) channel;
//
//
//            String currentDirectory = channelSftp.pwd();
//
//
//            SftpATTRS attrs = null;
//            try {
//                attrs = channelSftp.stat(currentDirectory + "/" + folderName);
//            } catch (Exception e) {
//                System.out.println(currentDirectory + "/" + folderName + " not found");
//            }
//
//            if (attrs != null) {
//                System.out.println("Directory exists IsDir=" + attrs.isDir());
//            } else {
//                System.out.println("Creating dir " + folderName);
//                channelSftp.mkdir(folderName);
//            }
//            channelSftp.cd(folderName);
////            File f = new File();
//            channelSftp.put(new FileInputStream(source), source.getName());
//            channelSftp.exit();
//            channel.disconnect();
//            session.disconnect();
//
//            r = "s";
//        } catch (Exception ex) {
//            r = "f";
//            ex.printStackTrace();
//        } finally {
//            if (channel != null) {
//                channel.disconnect();
//            }
//            if (session != null) {
//                session.disconnect();
//            }
//        }
//
//        return r;
//    }
//    public String upload(String ftpServer, String user, String password,
//                         String fileName, File source) {
////        System.out.println("ftp server upload user password filename  source " + ftpServer + " || " + user + " || " + password + " || " + fileName + " ||" + source);
//        String ftpSer = ftpServer.split("/")[0];
//        String folderName = ftpServer.split("/")[1];
//        String r = "f";
//        String SFTPHOST = ftpSer;
//        int SFTPPORT = 2240;
//
//        String SFTPWORKINGDIR = folderName;
//
//        Session session = null;
//        Channel channel = null;
//        ChannelSftp channelSftp = null;
//
//        try {
//            JSch jsch = new JSch();
//            session = jsch.getSession(user, ftpSer, SFTPPORT);
//            session.setPassword(password);
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);
//            session.connect();
//            channel = session.openChannel("sftp");
//            channel.connect();
//            channelSftp = (ChannelSftp) channel;
//
//
//            String currentDirectory = channelSftp.pwd();
//
//
//            SftpATTRS attrs = null;
//            try {
//                attrs = channelSftp.stat(currentDirectory + "/" + folderName);
//            } catch (Exception e) {
//                System.out.println(currentDirectory + "/" + folderName + " not found");
//            }
//
//            if (attrs != null) {
//                System.out.println("Directory exists IsDir=" + attrs.isDir());
//            } else {
//                System.out.println("Creating dir " + folderName);
//                channelSftp.mkdir(folderName);
//            }
//            channelSftp.cd(folderName);
////            File f = new File();
//            channelSftp.put(new FileInputStream(source), source.getName());
//            channelSftp.exit();
//            channel.disconnect();
//            session.disconnect();
//
//            r = "s";
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            r = "f";
//        } finally {
//            if (channel != null) {
//                channel.disconnect();
//            }
//            if (session != null) {
//                session.disconnect();
//            }
//        }
//
//        return r;
//
//    }
///// then download
//    public byte[] download(String ftpServer, String user, String password,
//                           String fileName) {
//        System.out.println("sftp file download user password filename  source " + ftpServer + " || " + user + " || " + password + " || " + fileName + " ||"  );
//        String ftpSer = ftpServer.split("/")[0];
//        String folderName = ftpServer.split("/")[1];
//        Channel channel =null;
//        Session session = null;
//        ChannelSftp sftp=null;
//        ByteArrayOutputStream bytesOut=null;
//        byte[] bytes = new byte[1024];
//        int SFTPPORT = 2240;
//
//        try {
//            JSch ssh = new JSch();
//            session = ssh.getSession(user, ftpSer, SFTPPORT);
//            // Remember that this is just for testing and we need a quick access, you can add an identity and known_hosts file to prevent
//            // Man In the Middle attacks
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);
//            session.setPassword(password);
//
//            session.connect(60000);
//            channel = session.openChannel("sftp");
//
//
//            channel.connect();
//             sftp = (ChannelSftp) channel;
//
//            sftp.cd(folderName);
//            // If you need to display the progress of the upload, read how to do it in the end of the article
//
//            // use the get method , if you are using android remember to remove "file://" and use only the relative path
////        sftp.get(fileName);
//           InputStream is= sftp.get(fileName);
//            bytes = IOUtils.toByteArray(is);
//
//        channel.disconnect();
//        session.disconnect();
////        is.close();
//        } catch (JSchException e) {
//            System.out.println(e.getMessage().toString());
//            e.printStackTrace();
//        } catch (SftpException e) {
//            System.out.println(e.getMessage().toString());
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.out.println(e.getMessage().toString());
//            e.printStackTrace();
//        }   finally {
//            if (channel != null) {
//                channel.disconnect();
//            }
//            if (session != null) {
//                session.disconnect();
//            }
//        }
//        return  bytes;
//    }
////connection file
//public String connect(String url, String username, String password,
//                           Integer port) {
//        System.out.println("sftp file connect user password filename  source " + url + " || " + username + " || " + password + " || " + port + " ||"  );
////        String ftpSer = ftpServer.split("/")[0];
////        String folderName = ftpServer.split("/")[1];
//        Channel channel =null;
//        Session session = null;
//        ChannelSftp sftp=null;
//        String success="f";
//        try {
//            JSch ssh = new JSch();
//            session = ssh.getSession(username, url, port);
//            // Remember that this is just for testing and we need a quick access, you can add an identity and known_hosts file to prevent
//            // Man In the Middle attacks
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);
//            session.setPassword(password);
//            session.connect();
//            channel = session.openChannel("sftp");
//            channel.connect();
////             sftp = (ChannelSftp) channel;
//            success="s";
//        } catch (JSchException e) {
////            System.out.println(e.getMessage().toString());
//            e.printStackTrace();
//            success="f";
//        }
//
//        return  success;
//    }
//
//
//
//
//}