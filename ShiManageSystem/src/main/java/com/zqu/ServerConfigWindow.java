package com.zqu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @aurhor Dedryck
 * @create 2023-11-27-17:06
 * @description:服务信息配置情况，我挂了梯子之后，会给我显示的具体的服务器Ip地址不是我连接热点/网络的IP地址
 *
 */
public class ServerConfigWindow {
//    可选二选一：
//    private static final String CONFIG_FILE_PATH = "server-config.properties";

    private static final String CONFIG_FILE_PATH = System.getProperty("user.home") + File.separator + "server-config.properties";

    public static void display(Stage parentStage, Stage systemManagementStage) {
        // 在显示界面前先初始化配置
        initializeConfig();

        Stage window = new Stage();
        window.setTitle("服务器配置信息");

        Properties properties = new Properties();
        File configFile = new File(CONFIG_FILE_PATH);

        // 尝试获取本机IP地址，如果失败则使用默认值127.0.0.1
        String localIp = getLocalIpAddress();
        if (localIp == null) {
            localIp = "127.0.0.1";
        }

        // 尝试加载配置文件
        boolean isConfigLoaded = false;
        // 尝试加载配置文件
        if (configFile.exists()) {
            try (FileInputStream input = new FileInputStream(configFile)) {
                properties.load(input);
                String decryptedIp = decrypt(properties.getProperty("server.ip", ""));
                String decryptedPort = decrypt(properties.getProperty("server.port", ""));

                // 检查解密后的值是否为 null
                if (decryptedIp != null && decryptedPort != null) {
                    properties.setProperty("server.ip", decryptedIp);
                    properties.setProperty("server.port", decryptedPort);
                } else {
                    showAlert("错误", "解密配置文件失败。");
                }
            } catch (IOException ex) {
                showAlert("错误", "配置文件加载失败，将使用默认值。");
            }
        }

        // UI 设置
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label ipLabel = new Label("服务器IP：");
        TextField ipTextField = new TextField();
        // 设置文本字段的初始值为本机IP地址
//        ipTextField.setText(localIp);
//        TextField ipTextField = new TextField(properties.getProperty("server.ip", ""));
//        TextField ipTextField = new TextField(properties.getProperty("server.ip", localIp));

        ipTextField.setText(isConfigLoaded ? properties.getProperty("server.ip") : localIp);
        Label portLabel = new Label("端口：");
//        TextField portTextField = new TextField(properties.getProperty("server.port", ""));
//        TextField portTextField = new TextField(properties.getProperty("server.port", ""));
        TextField portTextField = new TextField(properties.getProperty("server.port", ""));

        Button saveButton = new Button("确定");
        Button closeButton = new Button("关闭");

        grid.add(ipLabel, 0, 0);
        grid.add(ipTextField, 1, 0);
        grid.add(portLabel, 0, 1);
        grid.add(portTextField, 1, 1);
        grid.add(saveButton, 1, 2);
        grid.add(closeButton, 1, 3);

        saveButton.setOnAction(e -> {
            String encryptedIp = encrypt(ipTextField.getText());
            String encryptedPort = encrypt(portTextField.getText());

            if (encryptedIp != null && encryptedPort != null) {
                properties.setProperty("server.ip", encryptedIp);
                properties.setProperty("server.port", encryptedPort);

                try (FileOutputStream output = new FileOutputStream(configFile)) {
                    properties.store(output, null);
//                    System.out.println("Configuration saved: IP - " + encryptedIp + ", Port - " + encryptedPort); // 打印保存的数据用于调试
                    showAlert("配置保存", "配置已成功保存。");

//                  用于测试socket是否可以对服务器连接成功
//                    if (testServerConnection(ipTextField.getText(), portTextField.getText())) {
//                        showAlert("配置保存", "配置已成功保存，并且服务器连接测试成功。");
//                    } else {
//                        showAlert("配置保存", "配置已保存，但服务器连接测试失败。");
//                    }

                    window.close(); // 关闭当前窗口
                    systemManagementStage.show(); // 显示系统管理模块窗口
                } catch (IOException ex) {
                    showAlert("错误", "无法保存配置文件。");
                    ex.printStackTrace();
                }
            } else {
                showAlert("错误", "加密错误，无法保存配置。");
            }
        });

        closeButton.setOnAction(e -> window.close());

        Scene scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        window.initOwner(parentStage);
        window.showAndWait();
    }


    // AES 密钥。在实际应用中应该安全生成和存储此密钥。
    private static final String SECRET_KEY = "1234567890123456"; // 需要是16位

    //    使用了一个密钥库。并设置了密码
    private static PublicKey getPublicKey() throws Exception {
        // 创建一个 KeyStore 对象，用于管理密钥库
        KeyStore keyStore = KeyStore.getInstance("JKS");

        // 使用try-with-resources语句，确保在代码块结束时自动关闭InputStream
        try (InputStream keyStoreData = ServerConfigWindow.class.getClassLoader().getResourceAsStream("mykeystore.jks")) {
            // 加载密钥库数据，提供密钥库文件的InputStream和密码（这里是"123123"）
            keyStore.load(keyStoreData, "123123".toCharArray());
        }

        // 从密钥库中获取证书（Certificate），使用密钥对的别名 "mykeypair"
        Certificate cert = keyStore.getCertificate("mykeypair");

        // 从证书中获取公钥（PublicKey）
        return cert.getPublicKey();
    }

//  密钥
    private static PrivateKey getPrivateKey() throws Exception {
        // 创建一个 KeyStore 对象，用于管理密钥库
        KeyStore keyStore = KeyStore.getInstance("JKS");

        // 使用try-with-resources语句，确保在代码块结束时自动关闭InputStream
        try (InputStream keyStoreData = ServerConfigWindow.class.getClassLoader().getResourceAsStream("mykeystore.jks")) {
            // 加载密钥库数据，提供密钥库文件的InputStream和密码（这里是"123123"）
            keyStore.load(keyStoreData, "123123".toCharArray());
        }

        // 从密钥库中获取私钥（PrivateKey），使用密钥对的别名 "mykeypair" 和密钥密码 "123123"
        return (PrivateKey) keyStore.getKey("mykeypair", "123123".toCharArray());
    }



    private static String encrypt(String data) {
        try {
            PublicKey publicKey = getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
//            System.out.println("Encrypted data: " + encryptedString); // 打印加密数据用于调试
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static String decrypt(String data) {
        try {
            PrivateKey privateKey = getPrivateKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("解密失败: " + e.getMessage());
            return null;
        }
    }


    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void initializeConfig() {
        Properties properties = new Properties();
        File configFile = new File(CONFIG_FILE_PATH);

        // 尝试获取本机IP地址，如果失败则使用默认值127.0.0.1
        String ipToUse = getLocalIpAddress();
        if (ipToUse == null) {
            ipToUse = "127.0.0.1";
        }

        // 检查配置文件是否存在
        if (!configFile.exists()) {
            // 如果配置文件不存在，则创建一个新文件，并设置默认值
            try {
                configFile.createNewFile();
                // 设置默认IP和端口值
//                String defaultIp = "127.0.0.1";
                // 对默认值进行加密
                String encryptedIp = encrypt(ipToUse); ;
                String defaultPort = "8080";
                String encryptedPort = encrypt(defaultPort);

                if (encryptedIp != null && encryptedPort != null) {
                    properties.setProperty("server.ip", encryptedIp);
                    properties.setProperty("server.port", encryptedPort);
                    // 将这些加密后的默认值保存到新创建的配置文件中
                    try (FileOutputStream output = new FileOutputStream(configFile)) {
                        properties.store(output, null);
//                        System.out.println("使用默认加密值创建的新配置文件：");//调试代码
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
//                System.out.println("创建新配置文件时出错： " + e.getMessage());//调试代码
                showAlert("错误", "创建新配置文件时出现异常。");
//                return;
            }
        } else {
            // 如果配置文件已存在，加载并检查是否需要更新
            try (FileInputStream input = new FileInputStream(configFile)) {
                properties.load(input);

                // 检查IP和端口是否已加密或为空
                String ip = properties.getProperty("server.ip", "");
                String port = properties.getProperty("server.port", "");
                boolean updated = false;


                if (ip.isEmpty()) {
                    String encryptedIp = encrypt(ipToUse); // 使用本机IP或默认值
                    properties.setProperty("server.ip", encryptedIp);
                    updated = true;
                }

                if (port.isEmpty()) {
                    String defaultPort = "8080";
                    String encryptedPort = encrypt(defaultPort);
                    properties.setProperty("server.port", encryptedPort);
                    updated = true;
                }

                if (updated) {
                    try (FileOutputStream output = new FileOutputStream(configFile)) {
                        properties.store(output, null);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("错误", "初始化配置时出现异常。");
            }
        }
    }



    // 一个方法来检查字符串是否可能已加密
    private static boolean isEncrypted(String text) {
        // 假定加密的字符串长度大于一定的字符数
        return text.length() > 30 && text.matches("^[a-zA-Z0-9/+]+={0,2}$");
    }

    private static void showAlertAndReturn(String title, String message, Stage systemManagementStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        systemManagementStage.show(); // 显示系统管理模块窗口
    }

//测试类，测试是否可以连接成功
    private static boolean testServerConnection(String ip, String port) {
        try (Socket socket = new Socket()) {
            int timeout = 2000; // 设置连接超时时间（毫秒）
            socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)), timeout);
            return true; // 连接成功
        } catch (NumberFormatException e) {
            showAlert("错误", "端口号格式不正确。");
        } catch (IOException e) {
            showAlert("连接测试", "无法连接到服务器。请检查配置。");
        }
        return false; // 连接失败
    }

//    获取本机IP地址
//    private static String getLocalIpAddress() {
//        try {
//            InetAddress candidateAddress = null;
//            // 遍历所有的网络接口
//            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
//                NetworkInterface iface = ifaces.nextElement();
//                // 在所有的接口下再遍历IP
//                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
//                    InetAddress inetAddr = inetAddrs.nextElement();
//                    if (!inetAddr.isLoopbackAddress()) {
//                        if (inetAddr.isSiteLocalAddress()) {
//                            // 如果是site-local地址，就是它了
//                            return inetAddr.getHostAddress();
//                        } else if (candidateAddress == null) {
//                            // 如果不是site-local地址，先记下来，但继续寻找site-local地址
//                            candidateAddress = inetAddr;
//                        }
//                    }
//                }
//            }
//            if (candidateAddress != null) {
//                return candidateAddress.getHostAddress();
//            }
//            // 如果没有其他方法，则使用本地主机地址
//            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
//            return jdkSuppliedAddress.getHostAddress();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private static String getLocalIpAddress() {
        try {
            InetAddress preferredAddress = null;
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = ifaces.nextElement();
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress() && inetAddr instanceof Inet4Address) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 优先选择site-local地址
                            return inetAddr.getHostAddress();
                        } else if (preferredAddress == null) {
                            // 如果没有site-local地址，选择第一个非环回地址
                            preferredAddress = inetAddr;
                        }
                    }
                }
            }
            if (preferredAddress != null) {
                return preferredAddress.getHostAddress();
            }
            // 如果没有其他方法，则使用本地主机地址
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
