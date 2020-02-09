package com.zous.catmaster.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AccountStoreKey implements Serializable {

    private static final long serialVersionUID = -3772232486544864818L;

    private String accoutId;

    private String storeId;

    public AccountStoreKey() {
    }

    public AccountStoreKey(String accoutId, String storeId) {
        this.accoutId = accoutId;
        this.storeId = storeId;
    }

    public String getAccoutId() {
        return accoutId;
    }

    public void setAccoutId(String accoutId) {
        this.accoutId = accoutId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (storeId == null ? 0 : storeId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof AccountStoreKey)) return false;
        AccountStoreKey objKey = (AccountStoreKey) obj;
        if (accoutId.equalsIgnoreCase(objKey.getAccoutId())
                && storeId.equalsIgnoreCase(objKey.getStoreId()) ) {
            return true;
        }
        return false;
    }
}
