package com.vhealth.mapping;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "manager")
public class Manager implements Serializable {

	
	  /**
	 * 
	 */

	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long mId;

	    @Column(name = "studioName")
	    private String studioName;

		public long getmId() {
			return mId;
		}

		public void setmId(long mId) {
			this.mId = mId;
		}

		public String getStudioName() {
			return studioName;
		}

		public void setStudioName(String studioName) {
			this.studioName = studioName;
		}
	    
	    

}
