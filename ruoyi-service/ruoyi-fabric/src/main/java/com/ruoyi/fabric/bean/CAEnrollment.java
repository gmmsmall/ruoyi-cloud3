package com.ruoyi.fabric.bean;


import lombok.Getter;
import lombok.Setter;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.Serializable;
import java.security.PrivateKey;

@Getter
@Setter
public class CAEnrollment implements Enrollment, Serializable {
    private static final long serialVersionUID = 550416591376968096L;
    private PrivateKey key;
    private String cert;

    public CAEnrollment(PrivateKey pkey, String signedPem){
        this.key = pkey;
        this.cert = signedPem;
    }
    @Override
    public PrivateKey getKey() {
        return key;
    }

    @Override
    public String getCert() {
        return cert;
    }
}
