package com.ruoyi.fabric.utils;

import com.ruoyi.system.domain.CAEnrollment;
import com.ruoyi.system.domain.UserContext;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.sdk.exception.CryptoException;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    //Serialize user
    public static void writeUserContext(UserContext userContext) throws Exception {
        String directoryPath = "users/" + userContext.getAffiliation();
        String filePath = directoryPath + "/" + userContext.getName() + ".ser";
        File directory = new File(directoryPath);
        if (!directory.exists())
            directory.mkdirs();

        FileOutputStream file = new FileOutputStream(filePath);
        ObjectOutputStream out = new ObjectOutputStream(file);

        // Method for serialization of object
        out.writeObject(userContext);

        out.close();
        file.close();
    }

    //Deserialize user
    public static UserContext readUserContext(String affiliation, String username) throws Exception {
        String filePath = "users/" + affiliation + "/" + username + ".ser";
        File file = new File(filePath);
        if (file.exists()) {
            // Reading the object from a file
            FileInputStream fileStream = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileStream);

            // Method for deserialization of object
            UserContext uContext = (UserContext) in.readObject();

            in.close();
            fileStream.close();
            return uContext;
        }

        return null;
    }

    //Create enrollment from key and certificate files.
    public static CAEnrollment getEnrollment(String keyFolderPath, String keyFileName, String certFolderPath, String certFileName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, CryptoException {
        PrivateKey key = null;
        String certificate = null;
        InputStream isKey = null;
        BufferedReader brKey = null;

        try {

            isKey = new FileInputStream(keyFolderPath + File.separator + keyFileName);
            brKey = new BufferedReader(new InputStreamReader(isKey));
            StringBuilder keyBuilder = new StringBuilder();

            for (String line = brKey.readLine(); line != null; line = brKey.readLine()) {
                if (line.indexOf("PRIVATE") == -1) {
                    keyBuilder.append(line);
                }
            }

            certificate = new String(Files.readAllBytes(Paths.get(certFolderPath, certFileName)));

            byte[] encoded = DatatypeConverter.parseBase64Binary(keyBuilder.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("EC");
            key = kf.generatePrivate(keySpec);
        } finally {
            isKey.close();
            brKey.close();
        }

        CAEnrollment enrollment = new CAEnrollment(key, certificate);
        return enrollment;
    }

    //Usercontext convert to wallet
    public static void convertWallet(UserContext userContext) throws IOException {
        String certificate = userContext.getEnrollment().getCert();
        PrivateKey privateKey = userContext.getEnrollment().getKey();
        String mspid = userContext.getMspId();

        Path walletDirectory = Paths.get("src/main/resources/wallet");
        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);
        Wallet.Identity identity = Wallet.Identity.createIdentity(mspid, certificate, privateKey);
        wallet.put(userContext.getName(), identity);
    }

    public static void convertTLSWallet(UserContext userContext) throws IOException {
        String certificate = userContext.getEnrollment().getCert();
        PrivateKey privateKey = userContext.getEnrollment().getKey();
        String mspid = userContext.getMspId();

        Path walletDirectory = Paths.get("src/main/resources/tlswallet");
        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);
        Wallet.Identity identity = Wallet.Identity.createIdentity(mspid, certificate, privateKey);
        wallet.put(userContext.getName(), identity);
    }

    //delete directory
    public static void cleanUp() {
        String directoryPath = "users";
        File directory = new File(directoryPath);
        deleteDirectory(directory);
    }

    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        }

        // either file or an empty directory
        Logger.getLogger(Util.class.getName()).log(Level.INFO, "Deleting - " + dir.getName());
        return dir.delete();
    }
}
