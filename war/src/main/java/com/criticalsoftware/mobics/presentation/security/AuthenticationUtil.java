package com.criticalsoftware.mobics.presentation.security;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.llom.OMAttributeImpl;
import org.apache.axiom.om.impl.llom.OMElementImpl;
import org.apache.axiom.om.impl.llom.OMNamespaceImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationUtil.class);

    private static final String utf8Encoding = "UTF-8";

    private static final String datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static final String xsdSecextURL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    
    private static final String xsdUtilityURL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

    private static final String passwordDigest = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest";

    private static final String base64Binary = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";

    private static String[] auth(String customerPassword) throws UnsupportedEncodingException {
        String[] autenticationData = new String[3];

        // nonce
        byte[] nonceB = Long.toString(new Date().getTime()).getBytes(); // random stuff
        String nonce = Base64.encodeBase64String(nonceB);
        autenticationData[0] = nonce;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("nonce: {}", nonce);
        }

        // created
        String created = (new SimpleDateFormat(datePattern)).format(new Date()); // current
        byte[] createdB = created.getBytes(utf8Encoding); // timestamp
        autenticationData[1] = created;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("date created: [}", created);
        }

        // Password
        String password = Hex.encodeHexString(DigestUtils.sha(customerPassword));
        byte[] passwordB = password.getBytes(utf8Encoding);

        // do the magic
        byte[] combinedB = new byte[nonceB.length + createdB.length + passwordB.length];
        System.arraycopy(nonceB, 0, combinedB, 0, nonceB.length);
        System.arraycopy(createdB, 0, combinedB, nonceB.length, createdB.length);
        System.arraycopy(passwordB, 0, combinedB, nonceB.length + createdB.length, passwordB.length);

        String password64 = Base64.encodeBase64String(DigestUtils.sha(combinedB));
        autenticationData[2] = password64;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("password: {}", password64);
        }

        return autenticationData;
    }

    public static OMElement getAuthenticationHeader(String username, String password)
            throws UnsupportedEncodingException {
        String[] authenticationData = null;
        String nonce = null;
        String created = null;
        String password64 = null;

        authenticationData = auth(password);
        nonce = authenticationData[0];
        created = authenticationData[1];
        password64 = authenticationData[2];

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespaceImpl wsse = new OMNamespaceImpl(xsdSecextURL, "wsse");
        OMNamespaceImpl wsu = new OMNamespaceImpl(xsdUtilityURL, "wsu");

        OMElement securityHeader = new OMElementImpl("Security", wsse, fac);
        OMElement usernameTokenHeader = new OMElementImpl("UsernameToken", wsse, fac);
        OMElement usernameHeader = new OMElementImpl("Username", wsse, fac);
        usernameHeader.setText(username);

        OMElement passwordHeader = new OMElementImpl("Password", wsse, fac);
        passwordHeader.addAttribute(new OMAttributeImpl("Type", null, passwordDigest, fac));
        passwordHeader.setText(password64);

        OMElement nonceHeader = new OMElementImpl("Nonce", wsse, fac);
        nonceHeader.addAttribute(new OMAttributeImpl("EncodingType", null, base64Binary, fac));
        nonceHeader.setText(nonce);

        OMElement createdHeader = new OMElementImpl("Created", wsu, fac);
        createdHeader.setText(created);
        usernameTokenHeader.addChild(usernameHeader);
        usernameTokenHeader.addChild(passwordHeader);
        usernameTokenHeader.addChild(nonceHeader);
        usernameTokenHeader.addChild(createdHeader);

        securityHeader.addChild(usernameTokenHeader);

        return securityHeader;
    }
}
